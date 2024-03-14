package com.rlin.cfm_joy_manager.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.blankj.utilcode.util.ToastUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: (name: String, describe: String, () -> Unit) -> Unit,
) {
    var nameText by remember { mutableStateOf("") }
    var describeText by remember { mutableStateOf("") }
    val isUploading = remember {
        mutableStateOf(false)
    }

    AlertDialog(
        onDismissRequest = {
            onDismissRequest()
        },
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = "共享到云端",
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "将键位数据传输到云端数据库，作者不保证数据库的稳定",
                    style = MaterialTheme.typography.titleSmall
                )
                OutlinedTextField(
                    modifier = Modifier.padding(vertical = 8.dp),
                    label = {
                        Text(text = "键位名字(限15字)")
                    },
                    value = nameText,
                    singleLine = true,
                    onValueChange = {
                        if (it.length <= 15) {
                            nameText = it
                        }
                    })
                OutlinedTextField(
                    modifier = Modifier.padding(vertical = 8.dp),
                    label = {
                        Text(text = "键位描述(限30字)")
                    },
                    value = describeText,
                    singleLine = true,
                    onValueChange = {
                        if (it.length <= 30) {
                            describeText = it
                        }
                    })
                Box(
                    modifier = Modifier.padding(vertical = 8.dp),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        onDismissRequest()
                    }) {
                        Text(text = "取消")
                    }
                    ElevatedButton(
                        onClick = {
                            isUploading.value = true
                            if (nameText.isNotEmpty() && describeText.isNotEmpty()) {
                                onConfirmation(nameText, describeText) {
                                    isUploading.value = false
                                }
                            } else {
                                ToastUtils.showShort("有内容为空")
                            }
                        },
                        enabled = !isUploading.value
                    ) {
                        Text(text = "上传")
                    }
                }
            }
        }
    }
}