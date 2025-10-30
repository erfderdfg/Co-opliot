package com.app.co_opilot.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Chat (
    val id: String,
    @SerialName("user_one_id") val userOneId: String,
    @SerialName("user_two_id") val userTwoId: String,
    @SerialName("created_at") val createdAt: String
)