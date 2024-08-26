package com.rlin.cfm_joy_manager.entity

class User(val id: String, val joys: List<String>) {

    fun getJoyNumList(): List<Int> {
        val numList = mutableListOf<Int>()

        joys.forEach { joy ->
            val result = joy.split("_")
            var userId = "无"
            var joyNum = 1
            if (result.size == 3) {
                joyNum = result[1].toInt()
                userId = result[2].split(".")[0]
            } else {
                if (result[1].length > 6) {
                    userId = result[1].split(".")[0]
                }
            }
        }

        return emptyList()
    }
}

fun getJoyFileName(user: String, num: Int): String {
    return when (num) {
        1 -> {
            "CustomJoySticksConfig_2_$user.json"
        }
        2 -> {
            "CustomJoySticksConfig_3_$user.json"
        }
        else -> {
            "CustomJoySticksConfig_$user.json"
        }
    }
}