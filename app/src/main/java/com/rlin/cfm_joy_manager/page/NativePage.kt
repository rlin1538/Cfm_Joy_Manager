package com.rlin.cfm_joy_manager.page

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.rlin.cfm_joy_manager.utils.getJoyFiles
import com.rlin.cfm_joy_manager.utils.goViewJoySticks
import com.rlin.cfm_joy_manager.utils.readJoyFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun NativePage(
    viewJoySticks: (String) -> Unit
) {
    val context = LocalContext.current
    val joys =
        remember {
            mutableStateListOf<String>()
        }

    LaunchedEffect(true) {
        val fetchedData = withContext(Dispatchers.IO) {
            getJoyFiles(context)
        }
        fetchedData.forEach {
            joys.add(it)
        }
    }

    if (joys.isEmpty()) {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(modifier = Modifier.align(Alignment.Center)) {
                CircularProgressIndicator()
            }
        }
    } else {
        LazyColumn {
            items(joys) {
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
                                    Toast.makeText(context, "施工中", Toast.LENGTH_SHORT).show()
                                }) {
                                Text(text = "修改")
                            }
                            Button(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                onClick = {
                                    Toast.makeText(context, "施工中", Toast.LENGTH_SHORT).show()
                                }) {
                                Text(text = "共享")
                            }
                            ElevatedButton(onClick = {
                                val json = readJoyFile(it, context).trim()
                                goViewJoySticks(json, viewJoySticks)
                            }) {
                                Text(text = "查看")
                            }
                        }
                    }
                }
            }
        }
    }
}
