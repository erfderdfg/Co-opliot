package com.app.co_opilot.data

import com.app.co_opilot.data.repository.RelationshipRepository
import com.app.co_opilot.domain.Relationship
import com.app.co_opilot.domain.enums.RelationshipStatus
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue

class RelationshipRepositoryTest {
    private lateinit var relationshipRepository: RelationshipRepository

    @Before
    fun init() {
        relationshipRepository = RelationshipRepository()
    }

    @Test
    fun getAllRelationships_returnsInitialDummyData() = runBlocking {
        val result = relationshipRepository.getAllRelationships()
        assertEquals(2, result.size)
    }

    @Test
    fun getRelationshipsForUser_returnsCorrectRelationships_whenUserExists() = runBlocking {
        val result = relationshipRepository.getRelationshipsForUser("user1")
        assertEquals(1, result.size)
        assertEquals("rel1", result[0].id)
    }

    @Test
    fun getRelationshipsForUser_returnsEmptyList_whenUserNotFound() = runBlocking {
        val result = relationshipRepository.getRelationshipsForUser("unknown")
        assertTrue(result.isEmpty())
    }

    @Test
    fun addRelationship_successfullyAddsNewRelationship() = runBlocking {
        val newRel = Relationship("rel3", "user4", "user5", RelationshipStatus.LIKED_BY_USER1, "2025-10-09")
        relationshipRepository.addRelationship(newRel)

        val result = relationshipRepository.getAllRelationships()
        assertEquals(3, result.size)
        assertTrue(result.contains(newRel))
    }

    @Test
    fun findRelationship_returnsRelationship_whenExists() = runBlocking {
        val rel = relationshipRepository.findRelationship("user1", "user2")
        assertNotNull(rel)
        assertEquals("rel1", rel?.id)
    }

    @Test
    fun findRelationship_returnsSameResult_regardlessOfOrder() = runBlocking {
        val rel = relationshipRepository.findRelationship("user2", "user1")
        assertNotNull(rel)
        assertEquals("rel1", rel?.id)
    }

    @Test
    fun findRelationship_returnsNull_whenNotExists() = runBlocking {
        val rel = relationshipRepository.findRelationship("user10", "user11")
        assertNull(rel)
    }
}
