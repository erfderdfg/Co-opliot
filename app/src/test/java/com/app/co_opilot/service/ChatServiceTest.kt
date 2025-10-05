package com.app.co_opilot.service

import com.app.co_opilot.data.repository.ChatRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ChatServiceTest {

    private lateinit var chatRepo: ChatRepository
    private lateinit var chatService: ChatService

    @Before
    fun setup() {
        chatRepo = ChatRepository()
        chatService = ChatService(chatRepo)
    }

    @Test
    fun `sendMessage should create new chat if not exists`() = runBlocking {
        val senderId = "user1"
        val receiverId = "user2"

        val success = chatService.sendMessage(senderId, receiverId, "Hello!")

        assertTrue(success)

        // Ensure chat was created
        val chat = chatRepo.getChat(senderId, receiverId)
        assertNotNull(chat)

        // Ensure message was added
        val messages = chatRepo.getChatHistory(chat.id)
        assertEquals(1, messages.size)
        assertEquals("Hello!", messages.first().message)
    }

    @Test
    fun `sendMessage should append message to existing chat`() = runBlocking {
        val senderId = "user1"
        val receiverId = "user2"

        // Initialize chat manually first
        val chat = chatRepo.initSession(senderId, receiverId)

        // Send two messages
        chatService.sendMessage(senderId, receiverId, "Hi there!")
        chatService.sendMessage(receiverId, senderId, "Hey!")

        val messages = chatRepo.getChatHistory(chat.id)

        assertEquals(2, messages.size)
        assertEquals("Hi there!", messages[0].message)
        assertEquals("Hey!", messages[1].message)
    }

    @Test
    fun `loadChatHistory should return empty list if chat newly created`() = runBlocking {
        val userOne = "a"
        val userTwo = "b"

        val messages = chatService.loadChatHistory(userOne, userTwo)
        assertTrue(messages.isEmpty())
    }

    @Test
    fun `loadChatHistory should return existing messages`() = runBlocking {
        val userOne = "alice"
        val userTwo = "bob"

        val chat = chatRepo.initSession(userOne, userTwo)
        chatRepo.sendMessage(userOne, chat.id, "Hello Bob!")
        chatRepo.sendMessage(userTwo, chat.id, "Hey Alice!")

        val messages = chatService.loadChatHistory(userOne, userTwo)

        assertEquals(2, messages.size)
        assertEquals("Hello Bob!", messages.first().message)
    }
}
