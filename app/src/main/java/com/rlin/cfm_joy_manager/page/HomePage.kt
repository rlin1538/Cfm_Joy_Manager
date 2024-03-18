package com.rlin.cfm_joy_manager.page

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ToastUtils
import com.rlin.cfm_joy_manager.utils.CFM_ALPHA_DIR
import com.rlin.cfm_joy_manager.utils.DATABASE_TABLE_NAME
import com.rlin.cfm_joy_manager.utils.GlobalStatus
import com.rlin.cfm_joy_manager.utils.REQUEST_CODE_FOR_DIR
import com.rlin.cfm_joy_manager.utils.SupabaseHelper
import com.rlin.cfm_joy_manager.utils.startFor
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Count

@Composable
fun HomePage(mActivity: ComponentActivity, requestPermission: () -> Unit) {
    val context = LocalContext.current
    val cloudJoyNumber = remember {
        mutableStateOf("Loading")
    }

    val joyDirs = remember {
        mutableStateOf(GlobalStatus.CFM_DIR)
    }
    LaunchedEffect(key1 = true) {
        try {
            val res = SupabaseHelper.client.from(DATABASE_TABLE_NAME).select(head = true) {
                count(Count.EXACT)
            }.countOrNull()
            Log.d("HomePge", "键位数据" + res.toString())
            cloudJoyNumber.value = res.toString()
        } catch (e: Exception) {
            Log.d("HomePage", "主页键位数量获取失败")
        }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    if (AppUtils.isAppInstalled("com.eg.android.AlipayGphone")) {
                        val payUrl = "https://qr.alipay.com/fkx11731ehnvcoespxzxd08"
                        val scheme = "alipays://platformapi/startapp?appId=20000067&url=${payUrl}"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(scheme))
                        ActivityUtils.startActivity(intent)
                    } else {
                        ToastUtils.showShort("未安装支付宝APP")
                    }
                }
            ) {
                Icon(imageVector = Icons.Outlined.MonetizationOn, contentDescription = "Donate")
                Text(modifier = Modifier.padding(horizontal = 8.dp), text = "捐赠寒心")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Column {
                    Text(
                        modifier = Modifier
                            .padding(vertical = 4.dp),
                        text = "CFM键位工具  ",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Row(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .clickable {
                                val scheme =
                                    "https://github.com/rlin1538/Cfm_Joy_Manager"
                                val intent =
                                    Intent(Intent.ACTION_VIEW, Uri.parse(scheme))
                                ActivityUtils.startActivity(intent)
                            },
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(vertical = 4.dp),
                            text = "By Rlin寒心  ",
                            style = MaterialTheme.typography.titleMedium,
                            fontFamily = FontFamily.Cursive,
                            fontStyle = FontStyle.Italic
                        )
                        Icon(
                            imageVector = Icons.Outlined.Link,
                            contentDescription = "Project Github Page"
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                )

                Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                modifier = Modifier.padding(bottom = 8.dp),
                                text = "开始使用",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(text = "点击下方获取权限授权使用CFM的键位文件夹，授权后之后启动无需再次获取")
                            Box(
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                            ) {
                                Button(
                                    onClick = {
                                        startFor(joyDirs.value, mActivity, REQUEST_CODE_FOR_DIR)
                                    }
                                ) {
                                    Text("获取权限")
                                }
                            }
                            Text(text = "若以上方法无效，请安装Shizuku进行授权")
                            Row {
                                Box(
                                    modifier = Modifier
                                        .padding(vertical = 4.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            requestPermission()
                                        }
                                    ) {
                                        Text("获取Shizuku权限")
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .padding(4.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            val scheme =
                                                "https://shizuku.rikka.app/zh-hans/download/"
                                            val intent =
                                                Intent(Intent.ACTION_VIEW, Uri.parse(scheme))
                                            ActivityUtils.startActivity(intent)
                                        }
                                    ) {
                                        Text("安装Shizuku")
                                    }
                                }
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                modifier = Modifier.padding(bottom = 8.dp),
                                text = "键位码",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(text = "进入游戏后，在本地键位点击对应ID的方案修改，输入键位码可替换键位数据\n用户ID可以在游戏内个人中心查看，位于头像上方\n你可以上传本地的键位数据到云端，得到一个键位码")
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                modifier = Modifier.padding(bottom = 8.dp),
                                text = "统计数据",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(text = "当前数据库已有${cloudJoyNumber.value}个共享键位")
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                modifier = Modifier.padding(bottom = 8.dp),
                                text = "切换到体验服键位",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(text = "初次切换后需要再次点击上方的获取权限，使用Shizuku则无需再次授权")
                            Box(
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                            ) {
                                Button(
                                    onClick = {
                                        if (AppUtils.isAppInstalled("com.tencent.tmgp.cfalpha")) {
                                            joyDirs.value = CFM_ALPHA_DIR
                                            GlobalStatus.CFM_DIR = CFM_ALPHA_DIR
                                            ToastUtils.showShort("切换为体验服文件夹")
                                        } else {
                                            ToastUtils.showShort("此设备未安装cfm体验服")
                                        }
                                    }
                                ) {
                                    Text("切换到体验服")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}