package com.app.co_opilot.domain

data class Notification(
    val id: String,
    val userId: String,
    val caption: String,
    val title: String,
    val description: String,
    val notifiedAt: String
)
