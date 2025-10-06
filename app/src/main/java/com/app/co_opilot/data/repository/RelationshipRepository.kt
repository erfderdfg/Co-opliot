package com.app.co_opilot.data.repository

import com.app.co_opilot.domain.Relationship
import com.app.co_opilot.domain.enums.RelationshipStatus

/**
 * Repository layer handling relationship data operations.
 * Currently uses dummy data until Supabase integration.
 */
class RelationshipRepository {

    private val relationships = mutableListOf<Relationship>(
        Relationship("rel1", "user1", "user2", RelationshipStatus.LIKED_BY_USER1, "2025-10-04"),
        Relationship("rel2", "user2", "user3", RelationshipStatus.LIKED_BY_USER2, "2025-10-04")
    )

    /** Returns all relationships. */
    fun getAllRelationships(): List<Relationship> = relationships

    /** Returns all relationships for a specific user ID. */
    fun getRelationshipsForUser(userId: String): List<Relationship> =
        relationships.filter { it.userOneId == userId || it.userTwoId == userId }

    /** Adds a new relationship (dummy insert). */
    fun addRelationship(relationship: Relationship) {
        relationships.add(relationship)
    }

    /** Finds relationship between two specific users, if exists. */
    fun findRelationship(userOneId: String, userTwoId: String): Relationship? =
        relationships.find {
            (it.userOneId == userOneId && it.userTwoId == userTwoId) ||
            (it.userOneId == userTwoId && it.userTwoId == userOneId)
        }
}
