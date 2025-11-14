package com.app.co_opilot.data.repository.mock


import com.app.co_opilot.domain.Message
import java.util.*

open class MockMessageRepository(
    private val table: FakeMessageTable = FakeMessageTable()
) {

    suspend fun seed(messages: List<Message>) {
        table.insertAll(messages)
    }

    suspend fun getMessage(messageId: String): Message? =
        table.findById(messageId)

    suspend fun getMessagesByChat(chatId: String): List<Message> =
        table.findByChat(chatId)

    suspend fun sendMessage(
        chatId: String,
        senderId: String,
        text: String
    ): Boolean {
        val msg = Message(
            id = UUID.randomUUID().toString(),
            chatId = chatId,
            senderId = senderId,
            message = text,
            sentAt = Date().toInstant().toString()
        )
        table.insert(msg)
        return true
    }
}
