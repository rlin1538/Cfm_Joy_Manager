package com.rlin.cfm_joy_manager.page

import android.content.ClipData
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.blankj.utilcode.util.ToastUtils
import com.rlin.cfm_joy_manager.data.JoyDataViewModel
import com.rlin.cfm_joy_manager.utils.goViewJoySticks
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CloudPage(
    viewJoySticks: (String) -> Unit
) {
    val context = LocalContext.current

    val joyDataViewModel: JoyDataViewModel = viewModel()
    val data = joyDataViewModel.items.collectAsLazyPagingItems()

    val clipboardManager = LocalClipboardManager.current

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
                LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2)) {
                    items(count = data.itemCount) { index ->
                        data[index]?.let { item ->
                            ElevatedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
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
    }


}
