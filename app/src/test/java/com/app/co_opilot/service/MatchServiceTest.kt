package com.app.co_opilot.service

import com.app.co_opilot.data.repository.RelationshipRepository
import com.app.co_opilot.data.repository.UserRepository
import com.app.co_opilot.domain.User
import com.app.co_opilot.domain.Relationship
import com.app.co_opilot.domain.enums.RelationshipStatus
import com.app.co_opilot.domain.enums.Sections
import com.app.co_opilot.domain.profile.AcademicProfile
import com.app.co_opilot.domain.profile.BasicProfile
import com.app.co_opilot.domain.profile.CareerProfile
import com.app.co_opilot.domain.profile.SocialProfile
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class MatchServiceTest {
    private lateinit var userRepository: UserRepository
    private lateinit var relationshipRepository: RelationshipRepository
    private lateinit var service: MatchService

    private val userA = User(
        "userA",
        "Alice",
        "alice@test.com",
        null,
        "2025-01-01T00:00:00Z",
        "2025-01-01T00:00:00Z",
        BasicProfile(),
        AcademicProfile(faculty = "Engineering", major = listOf("Computer Science"), school = "Test University", academicyear = 3),
        CareerProfile(),
        SocialProfile(mbti = "INTJ", hobbies = listOf("Reading", "Coding"), interests = listOf("Technology", "Science"))
    )

    private val userB = User(
        "userB",
        "Bob",
        "bob@test.com",
        null,
        "2025-01-01T00:00:00Z",
        "2025-01-01T00:00:00Z",
        BasicProfile(),
        AcademicProfile(faculty = "Engineering", major = listOf("Computer Science"), school = "Test University", academicyear = 3),
        CareerProfile(),
        SocialProfile(mbti = "ENTJ", hobbies = listOf("Gaming", "Coding"), interests = listOf("Technology", "Music"))
    )

    private val userC = User(
        "userC",
        "Carol",
        "carol@test.com",
        null,
        "2025-01-01T00:00:00Z",
        "2025-01-01T00:00:00Z",
        BasicProfile(),
        AcademicProfile(faculty = "Arts", major = listOf("History"), school = "Test University", academicyear = 1),
        CareerProfile(),
        SocialProfile(mbti = "ISFP", hobbies = listOf("Painting", "Music"), interests = listOf("Art", "History"))
    )

    @Before
    fun setup() {
        userRepository = mock()
        relationshipRepository = mock()
        service = MatchService(userRepository, relationshipRepository)
    }

    @Test
    fun `getRecommendations returns all users except self`() = runTest {
        whenever(userRepository.getAllUsers()).thenReturn(listOf(userA, userB, userC))
        whenever(relationshipRepository.getAllRelationships()).thenReturn(emptyList())

        val recs = service.getRecommendations("userA", null, 10)

        assertEquals(2, recs.size)
        assertFalse(recs.any { it.id == "userA" })
        assertTrue(recs.any { it.id == "userB" })
        assertTrue(recs.any { it.id == "userC" })
    }

    @Test
    fun `getRecommendations filters out blocked users`() = runTest {
        whenever(userRepository.getAllUsers()).thenReturn(listOf(userA, userB, userC))

        val blockRelationship = Relationship("r1", "userA", "userB", RelationshipStatus.BLOCKED, "2025-01-01T00:00:00Z")
        whenever(relationshipRepository.getAllRelationships()).thenReturn(listOf(blockRelationship))

        val recs = service.getRecommendations("userA", null, 10)

        assertFalse(recs.any { it.id == "userB" })
        assertTrue(recs.any { it.id == "userC" })
    }

    @Test
    fun `getRecommendations filters out users who blocked the requester`() = runTest {
        whenever(userRepository.getAllUsers()).thenReturn(listOf(userA, userB, userC))

        val blockRelationship = Relationship("r1", "userB", "userA", RelationshipStatus.BLOCKED, "2025-01-01T00:00:00Z")
        whenever(relationshipRepository.getAllRelationships()).thenReturn(listOf(blockRelationship))

        val recsForA = service.getRecommendations("userA", null, 10)

        assertFalse(recsForA.any { it.id == "userB" })
    }

    @Test
    fun `getRecommendations respects limit parameter`() = runTest {
        val manyUsers = listOf(userA, userB, userC) + (1..20).map { i ->
            User("user$i", "User$i", "user$i@test.com", null, "2025-01-01T00:00:00Z", "2025-01-01T00:00:00Z",
                BasicProfile(), AcademicProfile(), CareerProfile(), SocialProfile())
        }
        whenever(userRepository.getAllUsers()).thenReturn(manyUsers)
        whenever(relationshipRepository.getAllRelationships()).thenReturn(emptyList())

        val recs = service.getRecommendations("userA", null, 5)

        assertEquals(5, recs.size)
    }

    @Test
    fun `getRecommendations works with different sections`() = runTest {
        whenever(userRepository.getAllUsers()).thenReturn(listOf(userA, userB, userC))
        whenever(relationshipRepository.getAllRelationships()).thenReturn(emptyList())

        val socialRecs = service.getRecommendations("userA", Sections.SOCIAL, 10)
        val academicRecs = service.getRecommendations("userA", Sections.ACADEMICS, 10)
        val coopRecs = service.getRecommendations("userA", Sections.COOP, 10)

        assertNotNull(socialRecs)
        assertNotNull(academicRecs)
        assertNotNull(coopRecs)

        assertEquals(2, socialRecs.size)
        assertEquals(2, academicRecs.size)
        assertEquals(2, coopRecs.size)
    }

    @Test
    fun `getRecommendations ranks similar users higher for academics section`() = runTest {
        whenever(userRepository.getAllUsers()).thenReturn(listOf(userA, userB, userC))
        whenever(relationshipRepository.getAllRelationships()).thenReturn(emptyList())

        val recs = service.getRecommendations("userA", Sections.ACADEMICS, 10)

        assertEquals(2, recs.size)
        assertEquals("userB", recs[0].id)
    }

    @Test
    fun `getRecommendations returns empty list when only self exists`() = runTest {
        whenever(userRepository.getAllUsers()).thenReturn(listOf(userA))
        whenever(relationshipRepository.getAllRelationships()).thenReturn(emptyList())

        val recs = service.getRecommendations("userA", null, 10)

        assertTrue(recs.isEmpty())
    }
}
