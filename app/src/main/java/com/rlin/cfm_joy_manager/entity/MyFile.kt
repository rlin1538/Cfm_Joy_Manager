package com.rlin.cfm_joy_manager.entity

import android.util.Log
import androidx.annotation.Keep
import com.rlin.cfm_joy_manager.IMyFile
import kotlin.system.exitProcess


@Keep
class MyFile : IMyFile.Stub() {
    override fun test() {
        Log.d("MyFile", "testxxxxxxxxxxxxxxxxxxxxx")
    }

    override fun destory() {
        Log.d("MyFile", "destoryyyyyyyyyyyyyyyyy")
        exitProcess(0)
    }
}

