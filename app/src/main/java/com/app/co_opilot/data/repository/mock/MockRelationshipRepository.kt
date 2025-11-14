package com.app.co_opilot.data.repository.mock

import com.app.co_opilot.domain.Relationship
import com.app.co_opilot.domain.enums.RelationshipStatus
import java.util.*

open class MockRelationshipRepository(
    private val table: FakeRelationshipTable = FakeRelationshipTable()
) {

    suspend fun seed(rels: List<Relationship>) {
        table.insertAll(rels)
    }

    suspend fun getAllRelationships(): List<Relationship> =
        table.getAll()

    suspend fun getRelationships(userId: String): List<Relationship> =
        table.findByUser(userId)

    suspend fun addRelationship(
        userOneId: String,
        userTwoId: String,
        status: RelationshipStatus
    ): Boolean {
        val rel = Relationship(
            id = UUID.randomUUID().toString(),
            userOneId = userOneId,
            userTwoId = userTwoId,
            status = status,
            createdAt = Date().toInstant().toString(),
            updatedAt = Date().toInstant().toString()
        )
        table.insert(rel)
        return true
    }

    suspend fun findRelationship(
        userOneId: String,
        userTwoId: String
    ): Relationship? =
        table.findPair(userOneId, userTwoId)

    suspend fun deleteRelationship(id: String): Boolean =
        table.deleteById(id)
}
