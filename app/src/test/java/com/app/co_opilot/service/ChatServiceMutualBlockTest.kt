package com.app.co_opilot.service

import com.app.co_opilot.data.repository.ChatRepository
import com.app.co_opilot.data.repository.RelationshipRepository
import com.app.co_opilot.data.repository.UserRepository
import com.app.co_opilot.domain.Chat
import com.app.co_opilot.domain.Relationship
import com.app.co_opilot.domain.enums.RelationshipStatus
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class ChatServiceMutualBlockTest {
    private lateinit var chatRepo: ChatRepository
    private lateinit var relationshipRepo: RelationshipRepository
    private lateinit var userRepo: UserRepository
    private lateinit var chatService: ChatService

    private val me = "me"

    @Before
    fun setup() {
        chatRepo = mock()
        relationshipRepo = mock()
        userRepo = mock()
        chatService = ChatService(chatRepo, relationshipRepo, userRepo)
    }

    @Test
    fun `initSession calls chatRepo initSession`() = runTest {
        val chat = Chat("c1", "me", "u2", Clock.System.now().toString())
        whenever(chatRepo.initSession("me", "u2")).thenReturn(chat)

        chatService.initSession("me", "u2")

        verify(chatRepo).initSession("me", "u2")
    }

    @Test
    fun `sendMessage creates chat and sends when chat does not exist`() = runTest {
        val chat = Chat("c1", "me", "u2", Clock.System.now().toString())
        whenever(chatRepo.getChat("me", "u2")).thenThrow(IllegalArgumentException())
        whenever(chatRepo.initSession("me", "u2")).thenReturn(chat)
        whenever(chatRepo.sendMessage("me", "c1", "hello")).thenReturn(true)

        val res = chatService.sendMessage("me", "u2", "hello")

        assertTrue(res)
        verify(chatRepo).initSession("me", "u2")
        verify(chatRepo).sendMessage("me", "c1", "hello")
    }

    @Test
    fun `loadChats filters blocked chats only`() = runTest {
        val chatNotBlocked = Chat("c1", me, "uA", Clock.System.now().toString())
        val chatBlocked = Chat("c2", me, "uB", Clock.System.now().toString())

        whenever(chatRepo.getChats(me)).thenReturn(listOf(chatNotBlocked, chatBlocked))

        whenever(relationshipRepo.findRelationship(me, "uA")).thenReturn(Relationship("rA1", me, "uA", RelationshipStatus.LIKED, "now"))
        whenever(relationshipRepo.findRelationship("uA", me)).thenReturn(Relationship("rA2", "uA", me, RelationshipStatus.LIKED, "now"))

        whenever(relationshipRepo.findRelationship(me, "uB")).thenReturn(Relationship("rB1", me, "uB", RelationshipStatus.BLOCKED, "now"))
        whenever(relationshipRepo.findRelationship("uB", me)).thenReturn(Relationship("rB2", "uB", me, RelationshipStatus.LIKED, "now"))

        val result = chatService.loadChats(me)

        assertEquals(1, result.size)
        assertEquals("c1", result.first().id)
    }

    @Test
    fun `blockUserAndDeleteChat delegates to repos`() = runTest {
        whenever(chatRepo.deleteChat("me","u2")).thenReturn(true)
        whenever(relationshipRepo.blockUser("me","u2")).thenReturn(true)

        val res = chatService.blockUserAndDeleteChat("me","u2")

        assertTrue(res)
        verify(chatRepo).deleteChat("me","u2")
        verify(relationshipRepo).blockUser("me","u2")
    }
}
