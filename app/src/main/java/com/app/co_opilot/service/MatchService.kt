package com.app.co_opilot.service

import com.app.co_opilot.data.repository.RelationshipRepository
import com.app.co_opilot.data.repository.UserRepository
import com.app.co_opilot.domain.User
import kotlin.math.sqrt

class MatchService(
    private val userRepository: UserRepository,
    private val relationshipRepository: RelationshipRepository
) {

    suspend fun getRecommendations(
        userId: String,
        limit: Int = 10,
        excludeExistingRelations: Boolean = true
    ): List<User> {

        val allUsers = userRepository.getAllUsers()
        println("MatchService: Fetched ${allUsers.size} users from repository")
        val self = allUsers.firstOrNull { it.id == userId } ?: return emptyList()
        val others = allUsers.filter { it.id != userId }

        val allRelationships = relationshipRepository.getAllRelationships()

        // Build adjacency list graph: Map<userId, Set<friendIds>>
        val graph = buildGraph(allRelationships)

        // Direct relations of self → optional exclusion
        val excluded = if (excludeExistingRelations) {
            graph[self.id] ?: emptySet()
        } else emptySet()

        val candidates = others.filter { it.id !in excluded }


        val scored = candidates.map { candidate ->
            val similarity = combinedSimilarity(self, candidate, graph)
            candidate to similarity
        }

        return scored.sortedByDescending { it.second }
            .take(limit)
            .map { it.first }
    }


    private fun combinedSimilarity(a: User, b: User, graph: Map<String, Set<String>>): Double {

        val interestScore = cosineSim(a.socialProfile.interests, b.socialProfile.interests)
        val hobbyScore = cosineSim(a.socialProfile.hobbies, b.socialProfile.hobbies)

        val academicScore = academicSimilarity(a, b)
        val mbtiScore = mbtiCompatibility(a.socialProfile.mbti, b.socialProfile.mbti)
        val graphScore = friendGraphScore(a.id, b.id, graph)

        return (
                0.32 * interestScore +
                        0.18 * hobbyScore +
                        0.22 * academicScore +
                        0.13 * mbtiScore +
                        0.15 * graphScore
                )
    }


    /**
     * Graph scoring:
     *
     * distance = 1 → direct friend → score = 1.0
     * distance = 2 → friend of friend → score = 0.6
     * distance = 3 → acquaintance → score = 0.3
     * distance > 3 or unreachable → score = 0.0
     */
    private fun friendGraphScore(a: String, b: String, graph: Map<String, Set<String>>): Double {

        if (a == b) return 1.0

        val visited = mutableSetOf<String>()
        val queue = ArrayDeque<Pair<String, Int>>() // Pair(node, distance)

        queue.add(a to 0)
        visited.add(a)

        while (queue.isNotEmpty()) {
            val (current, dist) = queue.removeFirst()

            if (current == b) {
                return when (dist) {
                    1 -> 1.0   // direct
                    2 -> 0.6   // friend of friend
                    3 -> 0.3   // extended network
                    else -> 0.0
                }
            }

            if (dist >= 3) continue // We don't care beyond 3 hops

            for (neighbor in graph[current] ?: emptySet()) {
                if (neighbor !in visited) {
                    visited.add(neighbor)
                    queue.add(neighbor to (dist + 1))
                }
            }
        }

        return 0.0
    }

    // Build adjacency graph from relationships
    private fun buildGraph(
        relationships: List<com.app.co_opilot.domain.Relationship>
    ): Map<String, Set<String>> {

        val graph = mutableMapOf<String, MutableSet<String>>()

        for (r in relationships) {
            val a = r.userOneId
            val b = r.userTwoId

            graph.getOrPut(a) { mutableSetOf() }.add(b)
            graph.getOrPut(b) { mutableSetOf() }.add(a)
        }

        return graph
    }

    private fun cosineSim(a: List<String>?, b: List<String>?): Double {
        if (a.isNullOrEmpty() || b.isNullOrEmpty()) return 0.0

        val vocab = (a + b).map { it.lowercase() }.toSet()
        val vecA = vocab.map { if (it in a.map(String::lowercase)) 1.0 else 0.0 }
        val vecB = vocab.map { if (it in b.map(String::lowercase)) 1.0 else 0.0 }

        val dot = vecA.zip(vecB).sumOf { it.first * it.second }
        val normA = sqrt(vecA.sumOf { it * it })
        val normB = sqrt(vecB.sumOf { it * it })

        return if (normA == 0.0 || normB == 0.0) 0.0 else dot / (normA * normB)
    }


    private fun academicSimilarity(a: User, b: User): Double {
        val pA = a.academicProfile
        val pB = b.academicProfile ?: return 0.0

        var score = 0.0

        if (pA?.faculty == pB.faculty) score += 0.6
        if (!pA?.major.isNullOrEmpty() && !pB.major.isNullOrEmpty()) {
            val overlap = pA.major!!.intersect(pB.major!!.toSet()).size
            score += overlap * 0.4
        }

        return score.coerceIn(0.0, 1.0)
    }

    private fun mbtiCompatibility(a: String?, b: String?): Double {
        if (a.isNullOrBlank() || b.isNullOrBlank()) return 0.0
        if (a == b) return 1.0

        var score = 0.0
        if (a[0] == b[0]) score += 0.25
        if (a[1] == b[1]) score += 0.25
        if (a[2] == b[2]) score += 0.25
        if (a[3] == b[3]) score += 0.25

        return score
    }
}
