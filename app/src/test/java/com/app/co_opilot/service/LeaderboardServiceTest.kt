package com.app.co_opilot.service

import com.app.co_opilot.data.repository.LeaderboardRepository
import com.app.co_opilot.data.repository.UserRepository
import com.app.co_opilot.domain.LeaderboardEntry
import com.app.co_opilot.domain.User
import com.app.co_opilot.domain.profile.AcademicProfile
import com.app.co_opilot.domain.profile.BasicProfile
import com.app.co_opilot.domain.profile.CareerProfile
import com.app.co_opilot.domain.profile.SocialProfile
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class LeaderboardServiceTest {
    private lateinit var leaderboardRepository: LeaderboardRepository
    private lateinit var userRepository: UserRepository
    private lateinit var service: LeaderboardService

    private val u1 = User("u1", "Alice", "a@u", null, "c1", "u1", BasicProfile(), AcademicProfile(), CareerProfile(), SocialProfile())
    private val u2 = User("u2", "Bob", "b@u", null, "c2", "u2", BasicProfile(), AcademicProfile(), CareerProfile(), SocialProfile())
    private val u3 = User("u3", "Cara", "c@u", null, "c3", "u3", BasicProfile(), AcademicProfile(), CareerProfile(), SocialProfile())

    @Before
    fun setup() {
        leaderboardRepository = mock()
        userRepository = mock()
        service = LeaderboardService(leaderboardRepository, userRepository)
    }

    @Test
    fun `getGlobalLeaderboard returns ranked users by score desc with ties`() = runBlocking {
        whenever(leaderboardRepository.getGlobalLeaderboard(any())).thenReturn(
            listOf(
                LeaderboardEntry("u2", 100),
                LeaderboardEntry("u3", 100),
                LeaderboardEntry("u1", 50)
            )
        )
        whenever(userRepository.getUser("u1")).thenReturn(u1)
        whenever(userRepository.getUser("u2")).thenReturn(u2)
        whenever(userRepository.getUser("u3")).thenReturn(u3)

        val ranked = service.getGlobalLeaderboard(10)

        assertEquals(3, ranked.size)
        assertEquals(1, ranked[0].rank)
        assertEquals("u2", ranked[0].user.id)
        assertEquals(1, ranked[1].rank)
        assertEquals("u3", ranked[1].user.id)
        assertEquals(3, ranked[2].rank)
        assertEquals("u1", ranked[2].user.id)
    }
}


