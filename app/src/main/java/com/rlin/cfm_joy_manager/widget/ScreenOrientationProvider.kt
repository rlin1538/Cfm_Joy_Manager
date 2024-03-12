package com.rlin.cfm_joy_manager.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import android.content.pm.ActivityInfo
import android.os.Build
import androidx.activity.ComponentActivity

@Composable
fun ScreenOrientationProvider(
    orientation: Int = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED,
    children: @Composable () -> Unit
) {
    val activity = (LocalContext.current as? ComponentActivity)
        ?: error("No access to activity. Use LocalActivity from androidx.activity:activity-compose")
    activity.apply {
        requestedOrientation = orientation
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O && orientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
            requestedOrientation =
                ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        }
    }

    children()
}