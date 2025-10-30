package com.app.co_opilot.data.repository

import com.app.co_opilot.domain.LeaderboardEntry

open class LeaderboardRepository {
    private val scores = mutableMapOf<String, Int>()

    suspend fun upsertScore(userId: String, score: Int) {
        scores[userId] = score
    }

    suspend fun getGlobalLeaderboard(limit: Int = 50): List<LeaderboardEntry> {
        return scores
            .entries
            .sortedByDescending { it.value }
            .take(limit)
            .map { LeaderboardEntry(userId = it.key, score = it.value) }
    }
}


