package com.rlin.cfm_joy_manager.page

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.blankj.utilcode.util.ToastUtils
import com.rlin.cfm_joy_manager.data.JoyDataViewModel
import com.rlin.cfm_joy_manager.entity.RESPONSE_FAILED
import com.rlin.cfm_joy_manager.entity.RESPONSE_SUCCESS
import com.rlin.cfm_joy_manager.utils.changeDocumentFile
import com.rlin.cfm_joy_manager.utils.fromCodeGetJoy
import com.rlin.cfm_joy_manager.utils.goViewJoySticks
import com.rlin.cfm_joy_manager.utils.readJoyFile
import com.rlin.cfm_joy_manager.widget.SearchJoyDialog
import com.rlin.cfm_joy_manager.widget.isScrollingUp
import kotlinx.coroutines.launch
import java.io.IOException

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CloudPage(
    viewJoySticks: (String) -> Unit
) {
    val context = LocalContext.current
    val lazyListState = rememberLazyStaggeredGridState()
    val scope = rememberCoroutineScope()

    val joyDataViewModel: JoyDataViewModel = viewModel()
    val data = joyDataViewModel.items.collectAsLazyPagingItems()

    val clipboardManager = LocalClipboardManager.current

    val openSearchJoyDialog = remember {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("CFM键位工具   By Rlin寒心")
                },
                actions = {
                    IconButton(onClick = {
                        data.refresh()
                        ToastUtils.showShort("刷新中")
                    }) {
                        Icon(imageVector = Icons.Filled.Refresh, contentDescription = "刷新")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    openSearchJoyDialog.value = true
                }) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Icon(Icons.Outlined.Search, contentDescription = "键位预览")
                    AnimatedVisibility(visible = lazyListState.isScrollingUp()) {
                        Text(
                            text = "键位预览", modifier = Modifier
                                .padding(start = 8.dp, top = 3.dp)
                        )
                    }
                }
            }
        },
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 12.dp)
        ) {
            if (data.itemCount == 0) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.align(Alignment.Center)) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    state = lazyListState
                ) {
                    items(count = data.itemCount) { index ->
                        data[index]?.let { item ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clickable { }
                            )
                            {
                                Column(
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = item.name,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Text(
                                            modifier = Modifier.padding(vertical = 8.dp),
                                            text = item.describe,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                    Divider(thickness = 0.dp)
                                    Row(
                                        modifier = Modifier.align(Alignment.End)
                                    ) {
                                        TextButton(
                                            onClick = {
                                                clipboardManager.setText(AnnotatedString(item.id.toString()))
                                                ToastUtils.showShort("已复制键位码：${item.id}")
                                            }) {
                                            Text(text = "复制")
                                        }
                                        TextButton(
                                            onClick = {
                                                goViewJoySticks(item.content, viewJoySticks)
                                            }) {
                                            Text(text = "查看")
                                        }
                                    }
                                }
                            }
                        }
                    }
                    val TAG = "CloudPage"
                    when (data.loadState.refresh) {
                        is LoadState.Loading -> {
                            Log.d(TAG, "正在加载")
                        }

                        is LoadState.Error -> {
                            when ((data.loadState.refresh as LoadState.Error).error) {
                                is IOException -> {
                                    Log.d(TAG, "网络未连接，可在这里放置失败视图")
                                }

                                else -> {
                                    Log.d(TAG, "网络未连接，其他异常")
                                }
                            }
                        }

                        is LoadState.NotLoading -> {
                            Log.d(TAG, "结束刷新")
                        }
                    }
                }
            }
        }

        if (openSearchJoyDialog.value)
            SearchJoyDialog(onDismissRequest = {
                openSearchJoyDialog.value = false
            }) { code, afterConfirm ->
                scope.launch {
                    val getJoyResponse = fromCodeGetJoy(code)

                    if (getJoyResponse.status == RESPONSE_SUCCESS) {
                        Log.d("CloudPage", getJoyResponse.content)
                        goViewJoySticks(getJoyResponse.content, viewJoySticks)
                    } else if (getJoyResponse.status == RESPONSE_FAILED) {
                        Log.d("CloudPage", getJoyResponse.content)
                        ToastUtils.showShort("键位码错误或网络异常")
                    }
                    afterConfirm()
                }
            }
    }
}
