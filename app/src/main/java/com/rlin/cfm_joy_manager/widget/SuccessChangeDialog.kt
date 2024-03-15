package com.rlin.cfm_joy_manager.widget


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun SuccessChangeDialog(
    onDismissRequest: () -> Unit,
    dialogTitle: String,
) {
    AlertDialog(
        icon = {
            Icon(Icons.Filled.CheckCircle, contentDescription = "成功")
        },
        title = {
            Text(text = "修改成功", style = MaterialTheme.typography.headlineLarge)
        },
        text = {
            Text(text = "请进入游戏键位页面任意切换键位方案来刷新\n并点击保存从而上传游戏云存档")
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {

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