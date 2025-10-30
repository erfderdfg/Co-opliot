package com.app.co_opilot.domain

import kotlinx.serialization.Serializable

@Serializable
data class LeaderboardEntry(
    val userId: String,
    val score: Int
)


