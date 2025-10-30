package com.app.co_opilot.service

import com.app.co_opilot.data.repository.LeaderboardRepository
import com.app.co_opilot.data.repository.UserRepository
import com.app.co_opilot.domain.LeaderboardEntry
import com.app.co_opilot.domain.User

class LeaderboardService(
    private val leaderboardRepository: LeaderboardRepository,
    private val userRepository: UserRepository
) {

    data class RankedUser(
        val rank: Int,
        val user: User,
        val score: Int
    )

    suspend fun getGlobalLeaderboard(limit: Int = 50): List<RankedUser> {
        val entries: List<LeaderboardEntry> = leaderboardRepository.getGlobalLeaderboard(limit)
        val ranked = mutableListOf<RankedUser>()
        var currentRank = 1
        var lastScore: Int? = null
        var numAtRank = 0

        for (entry in entries) {
            val user = userRepository.getUser(entry.userId) ?: continue
            val rank = if (lastScore != null && entry.score == lastScore) {
                numAtRank += 1
                currentRank
            } else {
                currentRank += numAtRank
                numAtRank = 1
                lastScore = entry.score
                currentRank
            }
            ranked.add(RankedUser(rank, user, entry.score))
        }
        return ranked
    }
}


