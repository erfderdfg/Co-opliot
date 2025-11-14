package com.app.co_opilot.service

import com.app.co_opilot.data.repository.RelationshipRepository
import com.app.co_opilot.data.repository.UserRepository
import com.app.co_opilot.domain.User
import com.app.co_opilot.domain.profile.AcademicProfile
import com.app.co_opilot.domain.profile.BasicProfile
import com.app.co_opilot.domain.profile.CareerProfile
import com.app.co_opilot.domain.profile.SocialProfile
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import java.util.Date

class UserServiceTest {

    private lateinit var userRepository: UserRepository
    private lateinit var relationshipRepository: RelationshipRepository
    private lateinit var userService: UserService

    private val testUser = User(
        "id1",
        "name",
        "t@uwaterloo.ca",
        null,
        Date().toString(),
        Date().toString(),
        BasicProfile(),
        AcademicProfile(),
        CareerProfile(),
        SocialProfile()
    )

    @Before
    fun setup() {
        userRepository = mock()
        relationshipRepository = mock()
        userService = UserService(userRepository, relationshipRepository)
    }

    @Test
    fun getUser_returnsUser_whenUserFound() = runTest {
        whenever(userRepository.getUser("id1")).thenReturn(testUser)

        val result = userService.getUser("id1")

        assertNotNull(result)
        assertEquals("name", result?.name)
        verify(userRepository).getUser("id1")
    }

    @Test
    fun getUser_returnsNull_whenUserNotFound() = runTest {
        whenever(userRepository.getUser("idX")).thenReturn(null)

        val result = userService.getUser("idX")

        assertNull(result)
        verify(userRepository).getUser("idX")
    }

    @Test
    fun createUser_delegatesToRepository_whenCalled() = runTest {
        whenever(
            userRepository.createUser(
                any(),
                any(),
                any(),
                anyOrNull(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        ).thenReturn(Unit)

        userService.createUser(
            testUser.id,
            testUser.name,
            testUser.email,
            testUser.avatarUrl,
            Date(testUser.createdAt),
            Date(testUser.updatedAt),
            testUser.basicProfile,
            testUser.academicProfile,
            testUser.careerProfile,
            testUser.socialProfile
        )

        verify(userRepository).createUser(
            eq(testUser.id),
            eq(testUser.name),
            eq(testUser.email),
            eq(testUser.avatarUrl),
            any(),
            any(),
            any(),
            any(),
            any(),
            any()
        )
    }

    @Test
    fun updateUserProfiles_returnsTrue_whenUpdateSucceeds() = runTest {
        whenever(userRepository.updateUserProfiles(eq("id1"), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull()))
            .thenReturn(true)

        val result = userService.updateUserProfiles("id1")

        assertTrue(result)
        verify(userRepository).updateUserProfiles(eq("id1"), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull())
    }

    @Test
    fun updateUserProfiles_returnsFalse_whenUserNotFound() = runTest {
        whenever(userRepository.updateUserProfiles(eq("missing"), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull()))
            .thenReturn(false)

        val result = userService.updateUserProfiles("missing")

        assertFalse(result)
        verify(userRepository).updateUserProfiles(eq("missing"), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull())
    }

    @Test
    fun deleteUser_returnsTrue_whenSuccessful() = runTest {
        whenever(userRepository.deleteUser("id1")).thenReturn(true)

        val result = userService.deleteUser("id1")

        assertTrue(result)
        verify(userRepository).deleteUser("id1")
    }

    @Test
    fun deleteUser_returnsFalse_whenUserNotFound() = runTest {
        whenever(userRepository.deleteUser("idX")).thenReturn(false)

        val result = userService.deleteUser("idX")

        assertFalse(result)
        verify(userRepository).deleteUser("idX")
    }

    @Test
    fun getAllUsers_returnsUserList_whenUsersExist() = runTest {
        whenever(userRepository.getAllUsers()).thenReturn(listOf(testUser))

        val result = userService.getAllUsers()

        assertEquals(1, result.size)
        assertEquals("id1", result.first().id)
        verify(userRepository).getAllUsers()
    }

    @Test
    fun getAllUsers_returnsEmptyList_whenNoUsersExist() = runTest {
        whenever(userRepository.getAllUsers()).thenReturn(emptyList())

        val result = userService.getAllUsers()

        assertTrue(result.isEmpty())
        verify(userRepository).getAllUsers()
    }

    @Test
    fun login_doesNotCrash_whenCalled() = runTest {
        userService.login("test@uwaterloo.ca", "password")
        // TODO
    }

    @Test
    fun signup_doesNotCrash_whenCalled() = runTest {
        userService.signup("test@uwaterloo.ca", "password")
        // TODO
    }
}
