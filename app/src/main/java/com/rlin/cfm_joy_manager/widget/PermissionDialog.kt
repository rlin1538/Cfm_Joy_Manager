package com.rlin.cfm_joy_manager.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.rlin.cfm_joy_manager.utils.hasDirectionPermission

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionDialog(
    agreeEvent: () -> Unit,
    disagreeEvent: () -> Unit,
    dismissEvent: () -> Unit,
) {
    val contentResolver = LocalContext.current.contentResolver

}