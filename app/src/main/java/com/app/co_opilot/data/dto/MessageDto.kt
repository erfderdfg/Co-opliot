package com.app.co_opilot.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val id: String,
    val chat_id: String,
    val sender_id: String,
    val message: String,
    val sent_at: String
)