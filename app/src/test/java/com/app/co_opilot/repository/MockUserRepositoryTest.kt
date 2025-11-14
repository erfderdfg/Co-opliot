package com.app.co_opilot.repository


import com.app.co_opilot.data.repository.mock.MockUserRepository
import com.app.co_opilot.domain.User
import com.app.co_opilot.domain.profile.*
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*
import java.util.*

class MockUserRepositoryTest {

    private fun dummyUser(id: String = "u1") = User(
        id = id,
        name = "Test User",
        email = "test@example.com",
        avatarUrl = null,
        createdAt = Date().toString(),
        updatedAt = Date().toString(),
        basicProfile = BasicProfile(),
        academicProfile = AcademicProfile(),
        careerProfile = CareerProfile(),
        socialProfile = SocialProfile()
    )

    @Test
    fun `getUser returns null when not found`() = runTest {
        val repo = MockUserRepository()
        val result = repo.getUser("missing")
        assertNull(result)
    }

    @Test
    fun `createUser then getUser returns inserted user`() = runTest {
        val repo = MockUserRepository()
        val now = Date()

        repo.createUser(
            id = "u1",
            name = "Alice",
            email = "alice@example.com",
            avatarUrl = "avatar",
            createdAt = now,
            updatedAt = now,
            basicProfile = BasicProfile(),
            academicProfile = AcademicProfile(),
            careerProfile = CareerProfile(),
            socialProfile = SocialProfile()
        )

        val user = repo.getUser("u1")
        assertNotNull(user)
        assertEquals("Alice", user!!.name)
        assertEquals("alice@example.com", user.email)
    }

    @Test
    fun `updateUserProfiles replaces only provided profiles`() = runTest {
        val repo = MockUserRepository()
        val u = dummyUser("u1")
        repo.seed(listOf(u))

        val newBasic = BasicProfile(gender = Gender.OTHER)
        val updated = repo.updateUserProfiles("u1", basic = newBasic)
        assertTrue(updated)

        val result = repo.getUser("u1")!!
        assertEquals(Gender.OTHER, result.basicProfile.gender)
        // unchanged
        assertEquals(u.academicProfile, result.academicProfile)
    }

    @Test
    fun `deleteUser removes user`() = runTest {
        val repo = MockUserRepository()
        repo.seed(listOf(dummyUser("u1")))

        assertTrue(repo.deleteUser("u1"))
        assertNull(repo.getUser("u1"))
    }
}
