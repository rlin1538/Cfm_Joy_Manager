package com.rlin.cfm_joy_manager.utils

import android.util.Log
import com.rlin.cfm_joy_manager.entity.JoyCodeResponse
import com.rlin.cfm_joy_manager.entity.JoyData
import com.rlin.cfm_joy_manager.entity.RESPONSE_FAILED
import com.rlin.cfm_joy_manager.entity.RESPONSE_SUCCESS
import io.github.jan.supabase.postgrest.postgrest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Random


suspend fun uploadJoy(name: String, describe: String, content: String): Int {
    try {
        val random = Random()
        val number = random.nextInt(1000000)
        val res = SupabaseHelper.client.postgrest[DATABASE_TABLE_NAME].insert(
            value = JoyData(
                id = number,
                name = name,
                describe = describe,
                content = content,
                created_at = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
            )
        ) {
            select()
        }.decodeSingle<JoyData>()
        Log.d("RequestUtil", "uploadJoy 成功, 键位码：${res.id}")

        return res.id
    } catch (e: Exception) {
        Log.d("RequestUtil", "uploadJoy 时出错：${e.message}")

        return -1
    }
}

suspend fun fromCodeGetJoy(code: String): JoyCodeResponse {
    try {
        val res = SupabaseHelper.client.postgrest[DATABASE_TABLE_NAME].select {
            filter {
                eq("id", code)
            }
        }
            .decodeSingle<JoyData>()

        return JoyCodeResponse(
            status = RESPONSE_SUCCESS,
            content = res.content
        )
    } catch (e: Exception) {
        return JoyCodeResponse(
            status = RESPONSE_FAILED,
            content = e.message?:""
        )
    }
}