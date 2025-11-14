package com.app.co_opilot.service

import com.app.co_opilot.data.repository.RelationshipRepository
import com.app.co_opilot.data.repository.UserRepository
import com.app.co_opilot.domain.Relationship
import com.app.co_opilot.domain.User
import com.app.co_opilot.domain.enums.RelationshipStatus
import com.app.co_opilot.domain.profile.AcademicProfile
import com.app.co_opilot.domain.profile.BasicProfile
import com.app.co_opilot.domain.profile.CareerProfile
import com.app.co_opilot.domain.profile.SocialProfile
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class MatchServiceTest {
    private lateinit var userRepository: UserRepository
    private lateinit var relationshipRepository: RelationshipRepository
    private lateinit var service: MatchService

    private val uMe = User("me", "Me", "me@u", null, "c", "u", BasicProfile(), AcademicProfile(), CareerProfile(), SocialProfile())
    private val u1 = User("u1", "Alice", "a@u", null, "c1", "u1", BasicProfile(), AcademicProfile(), CareerProfile(), SocialProfile())
    private val u2 = User("u2", "Bob", "b@u", null, "c2", "u2", BasicProfile(), AcademicProfile(), CareerProfile(), SocialProfile())
    private val u3 = User("u3", "Cara", "c@u", null, "c3", "u3", BasicProfile(), AcademicProfile(), CareerProfile(), SocialProfile())

    @Before
    fun setup() {
        userRepository = mock()
        relationshipRepository = mock()
        service = MatchService(userRepository, relationshipRepository)
    }

}


