package com.app.co_opilot.service

import com.app.co_opilot.data.repository.ChatRepository
import com.app.co_opilot.domain.Chat
import com.app.co_opilot.domain.Message
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.Date

class ChatServiceTest {

    private lateinit var chatRepo: ChatRepository
    private lateinit var chatService: ChatService

    @Before
    fun setup() {
        chatRepo = mock()
        chatService = ChatService(chatRepo)
    }

    @Test
    fun `sendMessage should create new chat if not exists`() {
        runBlocking {
            val senderId = "user1"
            val receiverId = "user2"
            val createdChat = Chat("chat123", senderId, receiverId, Clock.System.now().toString() )

            whenever(chatRepo.getChat(senderId, receiverId))
                .thenThrow(IllegalArgumentException())
            whenever(chatRepo.initSession(senderId, receiverId))
                .thenReturn(createdChat)
            whenever(chatRepo.sendMessage(senderId, createdChat.id, "Hello!"))
                .thenReturn(true)

            val result = chatService.sendMessage(senderId, receiverId, "Hello!")

            assertTrue(result)
            verify(chatRepo).initSession(senderId, receiverId)
            verify(chatRepo).sendMessage(senderId, createdChat.id, "Hello!")
        }
    }

    @Test
    fun `sendMessage should append message to existing chat`() {
        runBlocking {
            val senderId = "user1"
            val receiverId = "user2"
            val chat = Chat("chat123", senderId, receiverId, Clock.System.now().toString() )

            whenever(chatRepo.getChat(any(), any())).thenReturn(chat)
            whenever(chatRepo.sendMessage(any(), any(), any())).thenReturn(true)

            chatService.sendMessage(senderId, receiverId, "Hi there!")
            chatService.sendMessage(receiverId, senderId, "Hey!")

            verify(chatRepo).sendMessage(senderId, chat.id, "Hi there!")
            verify(chatRepo).sendMessage(receiverId, chat.id, "Hey!")
        }
    }

    @Test
    fun `loadChatHistory should return empty list if chat newly created`() {
        runBlocking {
            val u1 = "a"
            val u2 = "b"
            val newChat = Chat("chat123", u1, u2, Clock.System.now().toString() )

            whenever(chatRepo.getChat(u1, u2)).thenThrow(IllegalArgumentException())
            whenever(chatRepo.initSession(u1, u2)).thenReturn(newChat)
            whenever(chatRepo.getChatHistory(newChat.id)).thenReturn(emptyList())

            val result = chatService.loadChatHistory(u1, u2)

            assertTrue(result.isEmpty())
            verify(chatRepo).initSession(u1, u2)
        }
    }

    @Test
    fun `loadChatHistory should return existing messages`() {
        runBlocking {
            val u1 = "alice"
            val u2 = "bob"
            val chat = Chat("chat123", u1, u2, Clock.System.now().toString() )
            val msgs = listOf(
                Message("m1", chat.id, u1, "Hello Bob!",  Clock.System.now().toString()),
                Message("m2", chat.id, u2, "Hey Alice!", Clock.System.now().toString())
            )

            whenever(chatRepo.getChat(u1, u2)).thenReturn(chat)
            whenever(chatRepo.getChatHistory(chat.id)).thenReturn(msgs)

            val result = chatService.loadChatHistory(u1, u2)

            assertEquals(2, result.size)
            assertEquals("Hello Bob!", result.first().message)
            verify(chatRepo).getChatHistory(chat.id)
        }
    }
}
