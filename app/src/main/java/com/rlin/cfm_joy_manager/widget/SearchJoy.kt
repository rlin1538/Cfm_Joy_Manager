package com.rlin.cfm_joy_manager.widget


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.blankj.utilcode.util.ToastUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchJoyDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: (code: String, () -> Unit) -> Unit,
) {
    var codeText by remember { mutableStateOf("") }

    val isChanging = remember {
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
                .height(300.dp)
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
                    text = "键位预览",
                    style = MaterialTheme.typography.headlineLarge
                )
                OutlinedTextField(
                    modifier = Modifier.padding(vertical = 8.dp),
                    label = {
                        Text(text = "输入键位码")
                    },
                    value = codeText,
                    singleLine = true,
                    onValueChange = {
                        if (it.length <= 8) {
                            codeText = it
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
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
                            isChanging.value = true
                            if (codeText.isNotEmpty()) {
                                onConfirmation(codeText) {
                                    isChanging.value = false
                                }
                            } else {
                                ToastUtils.showShort("有内容为空")
                                isChanging.value = false
                            }
                        },
                        enabled = !isChanging.value
                    ) {
                        if (isChanging.value) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(30.dp),
                                strokeWidth = 3.dp
                            )
                        } else {
                            Text(text = "查看")
                        }
                    }
                }
            }
        }
    }
}