package com.rlin.cfm_joy_manager

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rlin.cfm_joy_manager.entity.Screen
import com.rlin.cfm_joy_manager.page.CloudPage
import com.rlin.cfm_joy_manager.page.HomePage
import com.rlin.cfm_joy_manager.page.NativePage
import com.rlin.cfm_joy_manager.ui.theme.Cfm_Joy_ManagerTheme
import com.rlin.cfm_joy_manager.utils.REQUEST_CODE_FOR_DIR
import com.rlin.cfm_joy_manager.utils.hasDirectionPermission
import com.rlin.cfm_joy_manager.widget.JoySticksViewer


class MainActivity : ComponentActivity() {
    private lateinit var mNavController: NavHostController
    private val mActivity = this

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var showDialog by remember {
                mutableStateOf(!hasDirectionPermission(contentResolver))
            }
            Log.d("MainActivity", "是否弹出权限申请弹窗：${showDialog}")

            Cfm_Joy_ManagerTheme {
                MainPage()
                /*if (showDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            if (hasDirectionPermission(contentResolver)) {
                                showDialog = false
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    startFor(CFM_DIR, mActivity, REQUEST_CODE_FOR_DIR)
                                    if (hasDirectionPermission(contentResolver)) {
                                        showDialog = false
                                    }
                                }
                            ) {
                                Text("同意")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    finish()
                                }
                            ) {
                                Text("不同意")
                            }
                        },
                        title = { Text("授权提示") },
                        text = { Text("请同意授权才能使用应用") },
                    )
                } else {
                }*/
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val uri: Uri? = data?.data

        if (requestCode == REQUEST_CODE_FOR_DIR) {
            uri?.let {
                contentResolver.takePersistableUriPermission(
                    it, (Intent.FLAG_GRANT_READ_URI_PERMISSION
                            or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                )
            } //关键是这里，这个就是保存这个目录的访问权限
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @Composable
    fun MainPage(modifier: Modifier = Modifier) {
        val context = LocalContext.current

        mNavController = rememberNavController()
        NavHost(navController = mNavController, startDestination = "page_home") {
            composable("page_home") {
                val navItems = listOf(
                    Screen.My,
                    Screen.Native,
                    Screen.Cloud,
                )
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            navItems.forEach { screen ->
                                NavigationBarItem(
                                    icon = { Icon(screen.icon, contentDescription = null) },
                                    label = { Text(stringResource(screen.resourceId)) },
                                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.My.route,
                        Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.My.route) {
                            HomePage(mActivity)
                        }
                        composable(Screen.Native.route) {
                            NativePage(viewJoySticks = viewJoySticks)
                        }
                        composable(Screen.Cloud.route) {
                            CloudPage(viewJoySticks = viewJoySticks)
                        }
                    }
                }
            }
            composable(route = "page_viewer/{json}",
                arguments = listOf<NamedNavArgument>(
                    navArgument("json") {
                        type = NavType.StringType
                        defaultValue = "null"
                    }
                )
            ) { it ->
                JoySticksViewerPage(it)
            }
            composable(route = "page_joys") {

            }
        }
    }

    private val viewJoySticks: (String) -> Unit = { json ->
        mNavController.navigate("Page_viewer/$json")
    }

    @Composable
    fun JoySticksViewerPage(it: NavBackStackEntry) {
        val configuration = LocalConfiguration.current
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        DisposableEffect(configuration, context, lifecycleOwner) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

            onDispose {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
        }
        val json = it.arguments?.getString("json")
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Gray
        ) {
            json?.let { it1 -> JoySticksViewer(it1) }
        }
    }
}

