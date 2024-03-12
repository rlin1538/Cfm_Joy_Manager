package com.rlin.cfm_joy_manager.entity

data class JoyButtonProperty(
    val xPos: Float = 0F,
    val yPos: Float = 0F,
    val Scale: Float = 1F,
    val MaxScale: Float = 2F,
    val MinScale: Float = 0.6F,
    val Scale2: Float,
    val MaxScale2: Float,
    val MinScale2: Float,
    val Alpha: Float = 1F,
    val LeftSide: Boolean,
    val UpSide: Boolean,
    val HorizontalLimit: Boolean,
    val VerticalLimit: Boolean,
    val BtnType: Int
) {


}