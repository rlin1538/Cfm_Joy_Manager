package com.rlin.cfm_joy_manager.page

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.blankj.utilcode.util.ToastUtils
import com.rlin.cfm_joy_manager.entity.RESPONSE_FAILED
import com.rlin.cfm_joy_manager.entity.RESPONSE_SUCCESS
import com.rlin.cfm_joy_manager.entity.getJoyFileName
import com.rlin.cfm_joy_manager.utils.changeDocumentFile
import com.rlin.cfm_joy_manager.utils.fromCodeGetJoy
import com.rlin.cfm_joy_manager.utils.getJoyFiles
import com.rlin.cfm_joy_manager.utils.goViewJoySticks
import com.rlin.cfm_joy_manager.utils.readJoyFile
import com.rlin.cfm_joy_manager.utils.uploadJoy
import com.rlin.cfm_joy_manager.widget.ChangeJoyDialog
import com.rlin.cfm_joy_manager.widget.SuccessChangeDialog
import com.rlin.cfm_joy_manager.widget.SuccessUploadDialog
import com.rlin.cfm_joy_manager.widget.UploadDialog
import dev.souravdas.materialsegmentedbutton.SegmentedButton
import dev.souravdas.materialsegmentedbutton.SegmentedButtonDefaults
import dev.souravdas.materialsegmentedbutton.SegmentedButtonItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NativePage(
    viewJoySticks: (String) -> Unit
) {
    val TAG = "NativePage"

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("CFM键位工具   By Rlin寒心")
                }
            )
        },
    ) { paddingValues ->

        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val clipboardManager = LocalClipboardManager.current

        val loading = remember {
            mutableStateOf(true)
        }
        val joys =
            remember {
                mutableStateListOf<String>()
            }
        val users =
            remember { mutableListOf<String>() }
        val filterUsers =
            remember {
                mutableStateListOf<String>()
            }
        val openUploadDialog = remember {
            mutableStateOf(false)
        }
        val openSuccessUploadDialog = remember {
            mutableStateOf(false)
        }
        val openChangeJoyDialog = remember {
            mutableStateOf(false)
        }
        val openSuccessChangeDialog = remember {
            mutableStateOf(false)
        }

        val selectedJoy = remember {
            mutableStateOf("")
        }
        val joyCode = remember {
            mutableStateOf("")
        }

        var searchText by rememberSaveable { mutableStateOf("") }

        LaunchedEffect(true) {
            val fetchedData = withContext(Dispatchers.IO) {
                return@withContext getJoyFiles(context)
            }
            Log.d(TAG, "获取到的JoyFiles是：$fetchedData")
            fetchedData.forEach {
                joys.add(it)
                val result = it.split("_")
                var userId = ""
                if (result.size == 3) {
                    userId = result[2].split(".")[0]
                } else {
                    if (result[1].length > 6) {
                        userId = result[1].split(".")[0]
                    }
                }
                if (!users.contains(userId)) users.add(userId)
            }
            filterUsers.addAll(users)
            loading.value = false
        }

        if (loading.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(modifier = Modifier.align(Alignment.Center)) {
                    CircularProgressIndicator()
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                SearchBar(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                        .align(Alignment.CenterHorizontally),
                    query = searchText,
                    onQueryChange = {
                        searchText = it
                        filterUsers.clear()
                        filterUsers.addAll(users.filter { user ->
                            user.contains(searchText)
                        })
                        println(filterUsers.toList())
                    },
                    onSearch = {},
                    active = false,
                    onActiveChange = {},
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search"
                        )
                    },
                    placeholder = {
                        Text(text = "输入用户ID")
                    },
                    shape = SearchBarDefaults.dockedShape,
                    windowInsets = WindowInsets(top = 0.dp)
                ) {

                }
                if (filterUsers.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        items(filterUsers) {
                            val viewBtn = remember {
                                mutableStateOf(true)
                            }
                            val selectJoyNum = remember {
                                mutableIntStateOf(0)
                            }
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "用户ID：$it ",
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                    }
                                    //Text(text = it)
                                    SegmentedButton(
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        items = listOf(
                                            SegmentedButtonItem(
                                                title = { Text(text = "键位一") },
                                                onClick = {
                                                    selectJoyNum.intValue = 0
                                                }
                                            ),
                                            SegmentedButtonItem(
                                                title = { Text(text = "键位二") },
                                                onClick = {
                                                    selectJoyNum.intValue = 1
                                                }
                                            ),
                                            SegmentedButtonItem(
                                                title = { Text(text = "键位三") },
                                                onClick = {
                                                    selectJoyNum.intValue = 2
                                                }
                                            ),
                                        ),
                                        cornerRadius = SegmentedButtonDefaults.segmentedButtonCorners(
                                            60
                                        ) //or you can individually mention each corners here
                                    )
                                    Row(
                                        modifier = Modifier.align(Alignment.End)
                                    ) {

                                        OutlinedButton(
                                            modifier = Modifier.padding(horizontal = 8.dp),
                                            onClick = {
                                                selectedJoy.value =
                                                    getJoyFileName(it, selectJoyNum.intValue)
                                                openChangeJoyDialog.value = true
                                            }) {
                                            Text(text = "修改")
                                        }
                                        Button(
                                            modifier = Modifier.padding(horizontal = 8.dp),
                                            onClick = {
                                                selectedJoy.value =
                                                    getJoyFileName(it, selectJoyNum.intValue)
                                                openUploadDialog.value = true
                                            }) {
                                            Text(text = "共享")
                                        }
                                        ElevatedButton(
                                            onClick = {
                                                viewBtn.value = false
                                                scope.launch {
                                                    val json = readJoyFile(
                                                        getJoyFileName(
                                                            it,
                                                            selectJoyNum.intValue
                                                        ), context
                                                    ).trim()
                                                    goViewJoySticks(json, viewJoySticks)
                                                    viewBtn.value = true
                                                }
                                            },
                                            enabled = viewBtn.value
                                        ) {
                                            if (viewBtn.value) {
                                                Text(text = "查看")
                                            } else {
                                                CircularProgressIndicator(
                                                    modifier = Modifier.size(30.dp),
                                                    strokeWidth = 3.dp
                                                )
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        Column(modifier = Modifier.align(Alignment.Center)) {
                            Text(text = "键位为空")
                        }
                    }
                }
            }
        }
        if (openUploadDialog.value) {
            UploadDialog(
                onConfirmation = { name, describe, id, afterUpload ->
                    scope.launch {
                        val result = uploadJoy(
                            name = name,
                            describe = describe,
                            customId = id,
                            content = readJoyFile(selectedJoy.value, context).trim()
                        )
                        joyCode.value = result.toString()
                        when (result) {
                            -1 -> {
                                ToastUtils.showShort("上传失败，请检查网络")
                            }
                            -2 -> {
                                ToastUtils.showShort("上传失败，键位读取错误，请重试")
                            }
                            -3 -> {
                                ToastUtils.showShort("键位码重复，请重试")
                            }

                            else -> {
                                openUploadDialog.value = false
                                ToastUtils.showShort("上传成功")
                                clipboardManager.setText(AnnotatedString(joyCode.value))
                                openSuccessUploadDialog.value = true
                            }
                        }
                        afterUpload()
                    }
                },
                onDismissRequest = { openUploadDialog.value = false }
            )
        }
        if (openSuccessUploadDialog.value) {
            SuccessUploadDialog(
                onDismissRequest = {
                    openSuccessUploadDialog.value = false
                },
                onConfirmation = {
                    clipboardManager.setText(AnnotatedString(joyCode.value))
                    ToastUtils.showShort("复制成功")
                },
                dialogTitle = joyCode.value
            )
        }

        if (openChangeJoyDialog.value) {
            ChangeJoyDialog(
                onDismissRequest = {
                    openChangeJoyDialog.value = false
                },
                onConfirmation = { code, afterConfirmation ->
                    scope.launch {
                        val getJoyResponse = fromCodeGetJoy(code)

                        if (getJoyResponse.status == RESPONSE_SUCCESS) {
                            Log.d("NativePage", getJoyResponse.content)
                            val result = changeDocumentFile(
                                selectedJoy.value,
                                getJoyResponse.content,
                                context
                            )
                            when (result) {
                                -1 -> {
                                    Log.d("NativePage", "替换失败")
                                    ToastUtils.showShort("替换失败")
                                }

                                1 -> {
                                    openChangeJoyDialog.value = false
                                    Log.d("NativePage", "替换成功")
                                    openSuccessChangeDialog.value = true
                                }
                            }
                        } else if (getJoyResponse.status == RESPONSE_FAILED) {
                            Log.d("NativePage", getJoyResponse.content)
                            ToastUtils.showShort("键位码错误或网络异常")
                        }
                        afterConfirmation()
                    }
                },
            )
        }

        if (openSuccessChangeDialog.value) {
            SuccessChangeDialog(
                onDismissRequest = {
                    openSuccessChangeDialog.value = false
                },
                dialogTitle = joyCode.value
            )
        }
    }
}
