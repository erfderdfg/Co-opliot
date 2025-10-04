package com.app.co_opilot.domain

data class User(
    val id: String,
    val name: String,
    val email: String,
    val avatarUrl: String?,
    val createdAt: String
)