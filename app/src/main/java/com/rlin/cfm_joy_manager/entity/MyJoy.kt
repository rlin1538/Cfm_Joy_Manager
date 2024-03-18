package com.rlin.cfm_joy_manager.entity

import android.text.TextUtils
import android.util.Log
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.FileUtils
import com.rlin.cfm_joy_manager.IMyJoy
import kotlin.system.exitProcess


class MyJoy : IMyJoy.Stub() {
    private val TAG = "MyJoy"

    companion object {
        const val GET_JOY_FILES = 1

    }

    override fun destory() {
        exitProcess(0)
    }

    override fun getJoyFiles(fileName: String): List<String> {
        val res =
            FileUtils.listFilesInDir(fileName)

        return res.map {
            return@map it.name.substring(it.name.lastIndexOf('/') + 1)
        }.filter {
            (it.startsWith("CustomJoySticksConfig_")
                    && it.endsWith(
                ".json"
            ) && (!TextUtils.equals(
                it,
                "CustomJoySticksConfig_1.json"
            ) && !TextUtils.equals(
                it,
                "CustomJoySticksConfig_2.json"
            ) && !TextUtils.equals(it, "CustomJoySticksConfig_3.json"))
                    )
        }
    }

    override fun readJoyFile(fileName: String): String {
        var res = ""
        try {
            res =  FileIOUtils.readFile2String(fileName)
            Log.d(TAG, "读取结果: ${res.length}")
        } catch (e:Exception) {
            e.message?.let { Log.e(TAG, it) }
        }

        return res
    }

    override fun writeJoyFile(fileName: String, content: String): Int {
        return try {
            if (FileIOUtils.writeFileFromString(fileName, content)) {
                1
            } else {
                -1
            }
        } catch (e: Exception) {
            e.message?.let { Log.e(TAG, it) }
            -1
        }
    }
}