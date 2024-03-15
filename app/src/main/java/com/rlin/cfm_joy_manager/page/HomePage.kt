package com.rlin.cfm_joy_manager.page

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ToastUtils
import com.rlin.cfm_joy_manager.utils.CFM_DIR
import com.rlin.cfm_joy_manager.utils.DATABASE_TABLE_NAME
import com.rlin.cfm_joy_manager.utils.REQUEST_CODE_FOR_DIR
import com.rlin.cfm_joy_manager.utils.SupabaseHelper
import com.rlin.cfm_joy_manager.utils.startFor
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Count

@Composable
fun HomePage(mActivity: ComponentActivity) {
    val cloudJoyNumber = remember {
        mutableStateOf("Loading")
    }
    LaunchedEffect(key1 = true) {
        try {
            val res = SupabaseHelper.client.from(DATABASE_TABLE_NAME).select(head = true) {
                count(Count.EXACT)
            }.countOrNull()
            Log.d("HomePge", "键位数据"+res.toString())
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
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        modifier = Modifier
                            .padding(vertical = 4.dp),
                        text = "CFM键位工具  ",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        modifier = Modifier
                            .padding(vertical = 4.dp),
                        text = "By Rlin寒心  ",
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = FontFamily.Cursive,
                        fontStyle = FontStyle.Italic
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                )

                Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
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
                                    .align(Alignment.End)
                                    .padding(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        startFor(CFM_DIR, mActivity, REQUEST_CODE_FOR_DIR)
                                    }
                                ) {
                                    Text("获取权限")
                                }
                            }
                        }
                    }
                }
                Box(modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp)) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                modifier = Modifier.padding(bottom = 8.dp),
                                text = "键位码",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(text = "你可以上传本地的键位数据到云端，然后得到一个键位码\n点击修改，输入键位码即可替换本地键位数据\n用户ID可以在游戏内个人中心查看，位于头像上方")
                        }
                    }
                }
                Box(modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp)) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
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
            }
        }
    }
}