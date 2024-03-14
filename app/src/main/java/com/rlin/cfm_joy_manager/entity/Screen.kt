package com.rlin.cfm_joy_manager.entity

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector
import com.rlin.cfm_joy_manager.R

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Cloud : Screen("page_cloud", R.string.page_cloud, icon = Icons.Filled.Cloud)
    object Native : Screen("page_native", R.string.page_native, icon = Icons.Filled.Folder)
    object My : Screen("page_my", R.string.page_my, icon = Icons.Filled.Home)
}