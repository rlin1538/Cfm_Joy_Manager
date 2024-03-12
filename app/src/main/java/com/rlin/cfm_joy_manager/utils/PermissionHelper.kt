package com.rlin.cfm_joy_manager.utils

import android.content.ContentResolver

fun hasDirectionPermission(contentResolver: ContentResolver): Boolean{
    return contentResolver.persistedUriPermissions.find { it ->
        it.uri.toString().contains("com.tencent.tmgp.cf")
    } != null
}