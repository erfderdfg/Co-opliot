package com.app.co_opilot.data.repository.mock


import com.app.co_opilot.domain.Chat
import com.app.co_opilot.domain.Message
import java.util.*

open class MockChatRepository(
    private val chatTable: FakeChatTable = FakeChatTable(),
    private val messageTable: FakeMessageTable = FakeMessageTable()
) {

    suspend fun seedChats(chats: List<Chat>) {
        chatTable.insertAll(chats)
    }

    suspend fun seedMessages(messages: List<Message>) {
        messageTable.insertAll(messages)
    }

    suspend fun initSession(userOneId: String, userTwoId: String): Chat {
        val existing = chatTable.findByUsers(userOneId, userTwoId)
        if (existing != null) return existing

        val newChat = Chat(
            id = UUID.randomUUID().toString(),
            userOneId = userOneId,
            userTwoId = userTwoId,
            createdAt = Date().toInstant().toString()
        )
        chatTable.insert(newChat)
        return newChat
    }

    suspend fun getChat(userOneId: String, userTwoId: String): Chat {
        val chat = chatTable.findByUsers(userOneId, userTwoId)
        return chat ?: throw IllegalArgumentException("Chat not found")
    }

    suspend fun getChats(userId: String): List<Chat> =
        chatTable.findByUser(userId)

    suspend fun sendMessage(
        senderId: String,
        chatId: String,
        content: String
    ): Boolean {
        val msg = Message(
            id = UUID.randomUUID().toString(),
            chatId = chatId,
            senderId = senderId,
            message = content,
            sentAt = Date().toInstant().toString()
        )
        messageTable.insert(msg)
        return true
    }

    suspend fun getChatHistory(chatId: String): List<Message> =
        messageTable.findByChat(chatId)
}
