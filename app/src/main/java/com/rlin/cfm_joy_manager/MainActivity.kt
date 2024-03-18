package com.rlin.cfm_joy_manager

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.view.WindowCompat
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
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.rlin.cfm_joy_manager.entity.Screen
import com.rlin.cfm_joy_manager.page.CloudPage
import com.rlin.cfm_joy_manager.page.HomePage
import com.rlin.cfm_joy_manager.page.NativePage
import com.rlin.cfm_joy_manager.ui.theme.Cfm_Joy_ManagerTheme
import com.rlin.cfm_joy_manager.utils.GlobalStatus
import com.rlin.cfm_joy_manager.utils.REQUEST_CODE_FOR_DIR
import com.rlin.cfm_joy_manager.utils.hasDirectionPermission
import com.rlin.cfm_joy_manager.widget.JoySticksViewer
import rikka.shizuku.Shizuku
import rikka.shizuku.ShizukuProvider.MANAGER_APPLICATION_ID


class MainActivity : ComponentActivity() {
    private lateinit var mNavController: NavHostController
    private val mActivity = this

    companion object {
        const val SHIZUKU_PERMISSION_REQUEST_CODE = 13
    }


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        Shizuku.addBinderReceivedListenerSticky { GlobalStatus.shizukuStatus = true }
        Shizuku.addBinderDeadListener { GlobalStatus.shizukuStatus = false }
        setContent {
            var showDialog by remember {
                mutableStateOf(!hasDirectionPermission(contentResolver))
            }
            Log.d("MainActivity", "是否弹出权限申请弹窗：${showDialog}")

            Cfm_Joy_ManagerTheme {
                TransparentSystemBars()
                MainPage()
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
//
//    override fun onRequestPermissionResult(requestCode: Int, grantResult: Int) {
//        if (grantResult != PackageManager.PERMISSION_GRANTED) {
//            Log.d("MainActivity", "OnRequestPermissionResult")
//            finish();
//            return;
//        }
//        doSzkWork()
//    }

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
                        Modifier.padding(
                            start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                            end = innerPadding.calculateEndPadding(LayoutDirection.Rtl),
                            bottom = innerPadding.calculateBottomPadding()
                        )
                    ) {
                        composable(Screen.My.route) {
                            HomePage(mActivity) { requestPermission() }
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

    @RequiresApi(Build.VERSION_CODES.P)
    @Composable
    fun JoySticksViewerPage(it: NavBackStackEntry) {
        val configuration = LocalConfiguration.current
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        DisposableEffect(configuration, context, lifecycleOwner) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.attributes.apply {
                // Window级别的全屏（这里的代码可以）
                systemUiVisibility =
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE

                // 设置视图内容是否显示到异形切口区域
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    layoutInDisplayCutoutMode =
                        WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                }
            }
//            window.decorView.apply {
//                // 视图级全屏
//                systemUiVisibility =
//                    View.SYSTEM_UI_FLAG_FULLSCREEN or
//                            // 视图内容延伸到状态栏区域
//                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
//                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//            }
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

            onDispose {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                window.attributes.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT
//                window.decorView.apply {
//                    systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE or
//                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                }

            }
        }
        val json = it.arguments?.getString("json")
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Gray
        ) {
            json?.let { it1 -> JoySticksViewer(it1) }
            Box {
                Text(
                    modifier = Modifier.align(Alignment.TopCenter),
                    text = "键位查看仅供参考",
                    color = Color.Gray.copy(alpha = 0.7F)
                )
            }
        }
    }

    fun requestPermission() {
        if (AppUtils.isAppInstalled(MANAGER_APPLICATION_ID)) {
            if (GlobalStatus.shizukuStatus) {
                if (Shizuku.shouldShowRequestPermissionRationale()) {
                    Log.d(
                        "Shizuku",
                        "您已选择「拒绝且不再询问」该应用的权限申请，请手动打开授权管理器进行授权。"
                    )
                    ToastUtils.showShort("您已选择「拒绝且不再询问」该应用的权限申请，请手动打开授权管理器进行授权。")
                } else {
                    if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_DENIED) {
                        Shizuku.addRequestPermissionResultListener { requestCode, grantResult ->
                            val isGranted =
                                requestCode == SHIZUKU_PERMISSION_REQUEST_CODE && grantResult == PackageManager.PERMISSION_GRANTED

                            Log.d("Shizuku", "isGranted: $isGranted")
                            ToastUtils.showShort("授权结果为$isGranted")

                        }
                        Shizuku.requestPermission(SHIZUKU_PERMISSION_REQUEST_CODE)
                    } else {
                        Log.d("Shizuku", "isGranted: 已授权过")
                        ToastUtils.showShort("已授权过")
                    }
                }
            } else {
                launchShizukuManager()
            }
        } else {
            ToastUtils.showShort("你还没有安装Shizuku")
        }
    }

    fun launchShizukuManager() {
        packageManager.getLaunchIntentForPackage(MANAGER_APPLICATION_ID)?.let {
            startActivity(it)
        }
    }

}

@Composable
fun TransparentSystemBars() {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons,
            isNavigationBarContrastEnforced = false,
        )
    }
}