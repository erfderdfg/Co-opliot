package com.app.co_opilot.repository

import com.app.co_opilot.data.repository.mock.MockMessageRepository
import com.app.co_opilot.domain.Message
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*
import java.util.*

class MockMessageRepositoryTest {

    private fun msg(id: String, chatId: String) = Message(
        id = id,
        chatId = chatId,
        senderId = "sender",
        message = "hello",
        sentAt = Date().toInstant().toString()
    )

    @Test
    fun `getMessage returns null when missing`() = runTest {
        val repo = MockMessageRepository()
        assertNull(repo.getMessage("none"))
    }

    @Test
    fun `sendMessage inserts and can be fetched`() = runTest {
        val repo = MockMessageRepository()
        val ok = repo.sendMessage("chat1", "u1", "hi")
        assertTrue(ok)

        val list = repo.getMessagesByChat("chat1")
        assertEquals(1, list.size)
        assertEquals("hi", list.first().message)
    }

    @Test
    fun `getMessagesByChat filters properly`() = runTest {
        val repo = MockMessageRepository()
        repo.seed(
            listOf(
                msg("m1", "c1"),
                msg("m2", "c1"),
                msg("m3", "c2")
            )
        )

        val c1 = repo.getMessagesByChat("c1")
        assertEquals(2, c1.size)
        assertTrue(c1.all { it.chatId == "c1" })
    }
}
