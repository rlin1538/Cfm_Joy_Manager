package com.rlin.cfm_joy_manager.page

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.blankj.utilcode.util.ToastUtils
import com.rlin.cfm_joy_manager.entity.RESPONSE_FAILED
import com.rlin.cfm_joy_manager.entity.RESPONSE_SUCCESS
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

        val joys =
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

        LaunchedEffect(true) {
            val fetchedData = withContext(Dispatchers.IO) {
                return@withContext getJoyFiles(context)
            }
            Log.d(TAG, "获取到的JoyFiles是：$fetchedData")
            fetchedData.forEach {
                joys.add(it)
            }
        }

        if (joys.isEmpty()) {

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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(joys) {
                    val viewBtn = remember {
                        mutableStateOf(true)
                    }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp, end = 12.dp, top = 12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            val result = it.split("_")
                            var userId = "无"
                            var joyNum = 1
                            if (result.size == 3) {
                                joyNum = result[1].toInt()
                                userId = result[2].split(".")[0]
                            } else {
                                if (result[1].length > 6) {
                                    userId = result[1].split(".")[0]
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "用户ID：$userId 的键位$joyNum",
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                            Text(text = it)
                            Row(
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                OutlinedButton(
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    onClick = {
                                        selectedJoy.value = it
                                        openChangeJoyDialog.value = true
                                    }) {
                                    Text(text = "修改")
                                }
                                Button(
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    onClick = {
                                        selectedJoy.value = it
                                        openUploadDialog.value = true
                                    }) {
                                    Text(text = "共享")
                                }
                                ElevatedButton(
                                    onClick = {
                                        viewBtn.value = false
                                        scope.launch {
                                            val json = readJoyFile(it, context).trim()
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
        }
        if (openUploadDialog.value) {
            UploadDialog(
                onConfirmation = { name, describe, afterUpload ->
                    scope.launch {
                        val result = uploadJoy(
                            name = name,
                            describe = describe,
                            content = readJoyFile(selectedJoy.value, context).trim()
                        )
                        joyCode.value = result.toString()
                        when (result) {
                            -1 -> {
                                ToastUtils.showShort("上传失败")
                            }

                            else -> {
                                openUploadDialog.value = false
                                ToastUtils.showShort("上传成功")
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
