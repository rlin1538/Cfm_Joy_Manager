package com.rlin.cfm_joy_manager.entity

import kotlinx.serialization.Serializable

@Serializable
data class JoyData(
    val id: Int,
    val created_at: String,
    val name: String,
    val describe: String,
    val content: String
)

const val RESPONSE_SUCCESS = 1
const val RESPONSE_FAILED = -1

data class JoyCodeResponse(
    val status: Int,
    val content: String
)