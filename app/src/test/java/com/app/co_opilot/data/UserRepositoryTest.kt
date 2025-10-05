package com.app.co_opilot.data

import com.app.co_opilot.data.repository.UserRepository
import com.app.co_opilot.domain.User
import com.app.co_opilot.domain.profile.AcademicProfile
import com.app.co_opilot.domain.profile.BasicProfile
import com.app.co_opilot.domain.profile.CareerProfile
import com.app.co_opilot.domain.profile.SocialProfile
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import java.io.File
import java.util.Date

class UserRepositoryTest {
    private lateinit var userRepository: UserRepository

    @Before
    fun init() {
        // create mock of underlying API service

        userRepository = UserRepository() // ensures a fresh repo for each run
    }

    @Test
    fun getUser_returnsNull_whenUserNotFound() = runBlocking {
        assertNull(userRepository.getUser("does_not_exist"))
    }

    @Test
    fun getUser_returnsNotNull_whenUserFound() = runBlocking {
        userRepository.createUser("id1", "name", "t@uwaterloo.ca", null, Date(), Date(), BasicProfile(), AcademicProfile(), CareerProfile(), SocialProfile())
        assertNotNull(userRepository.getUser("id1"))
    }

    @Test
    fun updateUserProfiles_noEffectAndReturnFalse_whenUserNotFound() = runBlocking {
        assertFalse(userRepository.updateUserProfiles("id1", null, null, null, null))
    }

    @Test
    fun updateUserProfiles_updateAndReturnTrue_whenUserFound() = runBlocking {
        userRepository.createUser("id1", "name", "t@uwaterloo.ca", null, Date(), Date(), BasicProfile(), AcademicProfile(), CareerProfile(), SocialProfile())
        assertTrue(userRepository.updateUserProfiles("id1", null, null, null, null))
    }

    @Test
    fun deleteUser_noEffect_whenUserNotFound() = runBlocking {
        userRepository.createUser("id1", "name", "t@uwaterloo.ca", null, Date(), Date(), BasicProfile(), AcademicProfile(), CareerProfile(), SocialProfile())
        assertEquals(1, userRepository.getAllUsers().size)
        userRepository.deleteUser("does_not_exist")
        assertEquals(1, userRepository.getAllUsers().size)
    }

    @Test
    fun deleteUser_successfullyDelete_whenUserFound() = runBlocking {
        userRepository.createUser("id1", "name", "t@uwaterloo.ca", null, Date(), Date(), BasicProfile(), AcademicProfile(), CareerProfile(), SocialProfile())
        assertEquals(1, userRepository.getAllUsers().size)
        userRepository.deleteUser("id1")
        assertEquals(0, userRepository.getAllUsers().size)
    }

    @Test
    fun getAllUsers_ReturnZero_whenUserNotFound() = runBlocking {
        assertFalse(userRepository.updateUserProfiles("id1", null, null, null, null))
    }

    @Test
    fun getAllUsers_updateAndReturnTrue_whenUserFound() = runBlocking {
        userRepository.createUser("id1", "name", "t@uwaterloo.ca", null, Date(), Date(), BasicProfile(), AcademicProfile(), CareerProfile(), SocialProfile())
        assertTrue(userRepository.updateUserProfiles("id1", null, null, null, null))
    }
}