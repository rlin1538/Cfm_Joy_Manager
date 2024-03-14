package com.rlin.cfm_joy_manager.entity

import kotlinx.serialization.Serializable

@Serializable
data class JoyDataResponse(
    val id: Int,
    val created_at: String,
    val name: String,
    val describe: String,
    val content: String
)

