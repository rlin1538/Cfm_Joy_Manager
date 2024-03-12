package com.rlin.cfm_joy_manager.utils

import android.content.Context


fun getDrawableId(context: Context, `var`: String?): Int {
    return try {
        context.resources.getIdentifier(`var`, "drawable", context.packageName)
    } catch (e: Exception) {
        0
    }
}
