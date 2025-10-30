package com.app.co_opilot.service

import com.app.co_opilot.data.repository.ChatRepository
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

class ChatServiceFriendsTest {
    private lateinit var chatRepo: ChatRepository
    private lateinit var relationshipRepository: RelationshipRepository
    private lateinit var userRepository: UserRepository
    private lateinit var chatService: ChatService

    private val u1 = User("u1", "Alice", "a@u", null, "c1", "u1", BasicProfile(), AcademicProfile(), CareerProfile(), SocialProfile())
    private val u2 = User("u2", "Bob", "b@u", null, "c2", "u2", BasicProfile(), AcademicProfile(), CareerProfile(), SocialProfile())

    @Before
    fun setup() {
        chatRepo = ChatRepository()
        relationshipRepository = mock()
        userRepository = mock()
        chatService = ChatService(chatRepo, relationshipRepository, userRepository)
    }

    @Test
    fun `loadFriends returns all related users`() = runBlocking {
        whenever(relationshipRepository.getRelationships("me")).thenReturn(
            listOf(
                Relationship("r1", "me", "u1", RelationshipStatus.PENDING, "now"),
                Relationship("r2", "me", "u2", RelationshipStatus.PENDING, "now")
            )
        )
        whenever(userRepository.getUser("u1")).thenReturn(u1)
        whenever(userRepository.getUser("u2")).thenReturn(u2)

        val friends = chatService.loadFriends("me")

        assertEquals(2, friends.size)
        assertEquals(setOf("u1", "u2"), friends.map { it.id }.toSet())
    }
}


