package com.app.co_opilot.service

import com.app.co_opilot.data.repository.RelationshipRepository
import com.app.co_opilot.data.repository.UserRepository
import com.app.co_opilot.domain.User

class MatchService(
    private val userRepository: UserRepository,
    private val relationshipRepository: RelationshipRepository? = null
) {
    suspend fun getRecommendations(userId: String, limit: Int = 10, excludeExistingRelations: Boolean = true): List<User> {
        val allUsers = userRepository.getAllUsers()
        val selfFiltered = allUsers.filter { it.id != userId }

        val excludedIds: Set<String> = if (excludeExistingRelations && relationshipRepository != null) {
            relationshipRepository.getRelationships(userId).map { it.userTwoId }.toSet()
        } else emptySet()

        val candidates = selfFiltered.filter { it.id !in excludedIds }

        // Minimal heuristic: return first N users; real scoring can be added later
        return candidates.take(limit)
    }
}


