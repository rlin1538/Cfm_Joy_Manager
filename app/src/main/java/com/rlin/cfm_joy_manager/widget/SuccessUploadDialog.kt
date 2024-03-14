package com.rlin.cfm_joy_manager.widget

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun SuccessUploadDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
) {
    AlertDialog(
        icon = {
            Icon(Icons.Filled.CheckCircle, contentDescription = "成功")
        },
        title = {
            Text(text = dialogTitle, style = MaterialTheme.typography.headlineLarge)
        },
        text = {
            Text(text = "已自动复制到剪贴板，请牢记你的键位码")
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("再次复制")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("关闭")
            }
        }
    )
}