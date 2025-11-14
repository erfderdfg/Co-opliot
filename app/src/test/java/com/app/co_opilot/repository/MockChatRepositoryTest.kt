package com.app.co_opilot.repository



import com.app.co_opilot.data.repository.mock.MockChatRepository
import com.app.co_opilot.domain.Chat
import com.app.co_opilot.domain.Message
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*
import java.util.*

class MockChatRepositoryTest {

    private fun chat(id: String, u1: String, u2: String) = Chat(
        id = id,
        userOneId = u1,
        userTwoId = u2,
        createdAt = Date().toInstant().toString()
    )

    private fun msg(id: String, chatId: String) = Message(
        id = id,
        chatId = chatId,
        senderId = "u1",
        message = "hello",
        sentAt = Date().toInstant().toString()
    )

    @Test
    fun `initSession reuses existing chat`() = runTest {
        val repo = MockChatRepository()
        val existing = chat("c1", "u1", "u2")
        repo.seedChats(listOf(existing))

        val chat = repo.initSession("u1", "u2")
        assertEquals("c1", chat.id)
    }

    @Test
    fun `initSession creates new chat if none exists`() = runTest {
        val repo = MockChatRepository()

        val chat = repo.initSession("u1", "u2")
        assertEquals("u1", chat.userOneId)
        assertEquals("u2", chat.userTwoId)
    }

    @Test
    fun `getChat throws when not found`() = runTest {
        val repo = MockChatRepository()
        assertThrows(IllegalStateException::class.java) {
            runTest { repo.getChat("u1", "u2") }
        }
    }

    @Test
    fun `getChatHistory returns messages of chat`() = runTest {
        val repo = MockChatRepository()
        repo.seedMessages(
            listOf(
                msg("m1", "c1"),
                msg("m2", "c1"),
                msg("m3", "c2")
            )
        )

        val history = repo.getChatHistory("c1")
        assertEquals(2, history.size)
        assertTrue(history.all { it.chatId == "c1" })
    }
}
