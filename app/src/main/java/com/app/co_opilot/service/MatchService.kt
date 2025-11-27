package com.app.co_opilot.service

import com.app.co_opilot.data.repository.RelationshipRepository
import com.app.co_opilot.data.repository.UserRepository
import com.app.co_opilot.domain.User
import com.app.co_opilot.domain.enums.Sections
import com.app.co_opilot.domain.enums.RelationshipStatus
import kotlin.math.sqrt

class MatchService(
    private val userRepository: UserRepository,
    private val relationshipRepository: RelationshipRepository
) {

    suspend fun getRecommendations(
        userId: String,
        section: Sections?,
        limit: Int = 10
    ): List<User> {

        val allUsers = userRepository.getAllUsers()
        val self = allUsers.first { it.id == userId }

        val relationships = relationshipRepository.getAllRelationships()

        val others = allUsers.filter { candidate ->
            if (candidate.id == userId) return@filter false

            val userBlockedCandidate = relationships.any {
                it.userOneId == userId && it.userTwoId == candidate.id && it.status == RelationshipStatus.BLOCKED
            }
            val candidateBlockedUser = relationships.any {
                it.userOneId == candidate.id && it.userTwoId == userId && it.status == RelationshipStatus.BLOCKED
            }

            !(userBlockedCandidate || candidateBlockedUser)
        }

        val graph = buildGraph(relationships)

        val scored = others.map { candidate ->
            val score = when (section) {
                Sections.SOCIAL -> socialSimilarity(self, candidate, graph)
                Sections.ACADEMICS -> academicSimilarityScore(self, candidate)
                Sections.COOP -> coopSimilarity(self, candidate, graph)
                null -> socialSimilarity(self, candidate, graph)
            }

            candidate to score
        }

        return scored
            .sortedByDescending { it.second }
            .take(limit)
            .map { it.first }
    }

    private fun socialSimilarity(a: User, b: User, graph: Map<String, Set<String>>): Double {
        val interest = cosineSim(a.socialProfile.interests, b.socialProfile.interests)
        val hobbies = cosineSim(a.socialProfile.hobbies, b.socialProfile.hobbies)
        val mbti = mbtiCompatibility(a.socialProfile.mbti, b.socialProfile.mbti)
        val graphScore = friendGraphScore(a.id, b.id, graph)

        return (
                0.35 * interest +
                        0.25 * hobbies +
                        0.20 * mbti +
                        0.20 * graphScore
                )
    }


    private fun academicSimilarityScore(a: User, b: User): Double {
        val apA = a.academicProfile
        val apB = b.academicProfile ?: return 0.0

        if (apA == null) return 0.0

        var score = 0.0

        if (apA.faculty == apB.faculty) score += 0.4
        if (!apA.major.isNullOrEmpty() && !apB.major.isNullOrEmpty()) {
            score += 0.4 * apA.major!!.intersect(apB.major!!.toSet()).size
        }
        if (apA.school == apB.school) score += 0.1

        // Year difference smaller = better
        if (apA.academicyear != null && apB.academicyear != null) {
            val diff = kotlin.math.abs(apA.academicyear!! - apB.academicyear!!)
            score += when (diff) {
                0 -> 0.1
                1 -> 0.05
                else -> 0.0
            }
        }

        return score.coerceIn(0.0, 1.0)
    }


    private fun coopSimilarity(a: User, b: User, graph: Map<String, Set<String>>): Double {
        val cpA = a.careerProfile
        val cpB = b.careerProfile ?: return 0.0

        var score = 0.0

        // Industry match
        if (!cpA.industry.isNullOrBlank() && cpA.industry == cpB.industry)
            score += 0.4

        // Skill overlap
        score += 0.4 * intersectCount(cpA.skills, cpB.skills)

        // Past internship overlap
        score += 0.2 * intersectCount(cpA.pastInternships, cpB.pastInternships)

        // Friend-of-friend social relevance
        val foaf = friendGraphScore(a.id, b.id, graph)

        return (score * 0.8 + foaf * 0.2).coerceIn(0.0, 1.0)
    }


    private fun intersectCount(a: List<String>?, b: List<String>?): Double {
        if (a.isNullOrEmpty() || b.isNullOrEmpty()) return 0.0
        val overlap = a.toSet().intersect(b.toSet()).size
        val max = maxOf(a.size, b.size).coerceAtLeast(1)
        return overlap.toDouble() / max
    }


    private fun friendGraphScore(a: String, b: String, graph: Map<String, Set<String>>): Double {
        if (a == b) return 1.0

        val visited = mutableSetOf<String>()
        val queue = ArrayDeque<Pair<String, Int>>() // (node, distance)
        queue.add(a to 0)
        visited.add(a)

        while (queue.isNotEmpty()) {
            val (current, dist) = queue.removeFirst()

            if (current == b) {
                return when (dist) {
                    1 -> 1.0   // direct friend
                    2 -> 0.6   // friend of friend
                    3 -> 0.3   // acquaintance
                    else -> 0.0
                }
            }

            if (dist >= 3) continue

            for (neighbor in graph[current] ?: emptySet()) {
                if (neighbor !in visited) {
                    visited.add(neighbor)
                    queue.add(neighbor to (dist + 1))
                }
            }
        }

        return 0.0
    }

    // Build adjacency map
    private fun buildGraph(relationships: List<com.app.co_opilot.domain.Relationship>):
            Map<String, Set<String>> {

        val map = mutableMapOf<String, MutableSet<String>>()

        for (r in relationships) {
            val a = r.userOneId
            val b = r.userTwoId
            map.computeIfAbsent(a) { mutableSetOf() }.add(b)
            map.computeIfAbsent(b) { mutableSetOf() }.add(a)
        }

        return map
    }

    // -------------------------------------------------------------------------
    // Cosine similarity
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

    // -------------------------------------------------------------------------
    // MBTI compatibility
    private fun mbtiCompatibility(a: String?, b: String?): Double {
        if (a.isNullOrBlank() || b.isNullOrBlank()) return 0.0
        if (a == b) return 1.0

        var s = 0.0
        if (a[0] == b[0]) s += 0.25
        if (a[1] == b[1]) s += 0.25
        if (a[2] == b[2]) s += 0.25
        if (a[3] == b[3]) s += 0.25
        return s
    }
}
