package com.app.co_opilot.domain

data class Message (
    val id: String,
    val chatId: String,
    val message: String,
    val sentAt: String
)