package com.rlin.cfm_joy_manager.utils

class ViewJoySticksHelper {
}

fun goViewJoySticks(
    input: String,
    viewJoySticks: (String) -> Unit
) {
    var json = input
    val curlyIndex = matchCurlyBrace(json)
    if (curlyIndex < json.length) {
        json = json.substring(0, curlyIndex + 1);
    }
    if (json.isNotEmpty() && curlyIndex != 0) {
        viewJoySticks(json)
    }
}

fun matchCurlyBrace(str: String): Int {
    var leftCount = 0
    for ((index, c) in str.withIndex()) {
        when (c) {
            '{' -> leftCount++
            '}' -> {
                leftCount--
                if (leftCount < 0) return 0
            }
        }
        if (leftCount == 0) return index
    }
    return str.length
}