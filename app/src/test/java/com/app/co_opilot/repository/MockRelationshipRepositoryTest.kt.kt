package com.app.co_opilot.repository


import com.app.co_opilot.data.repository.mock.MockRelationshipRepository
import com.app.co_opilot.domain.Relationship
import com.app.co_opilot.domain.enums.RelationshipStatus
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*
import java.util.*

class MockRelationshipRepositoryTest {

    private fun rel(
        id: String,
        u1: String,
        u2: String,
        status: RelationshipStatus
    ) = Relationship(
        id = id,
        userOneId = u1,
        userTwoId = u2,
        status = status,
        createdAt = Date().toInstant().toString(),
        updatedAt = Date().toInstant().toString()
    )

    @Test
    fun `getRelationships filters by user_one_id`() = runTest {
        val repo = MockRelationshipRepository()
        repo.seed(
            listOf(
                rel("r1", "u1", "u2", RelationshipStatus.PENDING),
                rel("r2", "u2", "u1", RelationshipStatus.MATCHED),
                rel("r3", "u1", "u3", RelationshipStatus.MATCHED)
            )
        )

        val result = repo.getRelationships("u1")
        assertEquals(2, result.size)
        assertTrue(result.all { it.userOneId == "u1" })
    }

    @Test
    fun `addRelationship creates a new entry`() = runTest {
        val repo = MockRelationshipRepository()

        val ok = repo.addRelationship("u1", "u2", RelationshipStatus.PENDING)
        assertTrue(ok)

        val list = repo.getAllRelationships()
        assertEquals(1, list.size)
        assertEquals("u1", list.first().userOneId)
        assertEquals("u2", list.first().userTwoId)
    }

    @Test
    fun `findRelationship returns pair match`() = runTest {
        val repo = MockRelationshipRepository()
        val r = rel("r1", "u1", "u2", RelationshipStatus.MATCHED)
        repo.seed(listOf(r))

        val res = repo.findRelationship("u1", "u2")
        assertNotNull(res)
        assertEquals("r1", res!!.id)
    }

    @Test
    fun `deleteRelationship removes by id`() = runTest {
        val repo = MockRelationshipRepository()
        val r = rel("r1", "u1", "u2", RelationshipStatus.MATCHED)
        repo.seed(listOf(r))

        assertTrue(repo.deleteRelationship("r1"))
        assertTrue(repo.getAllRelationships().isEmpty())
    }
}
