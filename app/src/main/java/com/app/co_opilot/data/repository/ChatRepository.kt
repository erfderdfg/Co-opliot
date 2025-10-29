package com.app.co_opilot.data.repository

import com.app.co_opilot.domain.Chat
import com.app.co_opilot.domain.Message
import com.app.co_opilot.domain.User
import java.util.Date
import java.util.UUID

// ChatService owns it
class ChatRepository( /* Inject remote API (Supabase DB Client) service */ ) {

    // TODO: temporary data, modify when connecting to Supabase
    private var chats = mutableListOf<Chat>()
    private var messages = mutableListOf<Message>()

    suspend fun initSession(userOneId: String, userTwoId: String): Chat {
        val existingChat = chats.find {
            (it.userOneId == userOneId && it.userTwoId == userTwoId) ||
                    (it.userOneId == userTwoId && it.userTwoId == userOneId)
        }

        if (existingChat != null) {
            return existingChat
        }

        val newChat = Chat(
            id = UUID.randomUUID().toString(),
            userOneId = userOneId,
            userTwoId = userTwoId,
            createdAt = Date().toString()
        )

        chats.add(newChat)
        return newChat
    }

    suspend fun sendMessage(senderId: String, chatId: String, message: String): Boolean {
        val chat = chats.find { it.id == chatId } ?: return false

        val message = Message(
            id = UUID.randomUUID().toString(),
            chatId = chat.id,
            senderId = senderId,
            message = message,
            sentAt = Date().toString()
        )

        messages.add(message)
        return true
    }


    suspend fun getChat(userOneId: String, userTwoId: String): Chat {
        val chat = chats.find { (it.userOneId == userOneId && it.userTwoId == userTwoId) ||
                            (it.userOneId == userTwoId && it.userTwoId == userOneId) }

        if (chat == null) {
            throw IllegalArgumentException("Chat Session does not exist")
        }

        return chat
    }

    suspend fun getChatHistory(chatId: String): List<Message> {
        return messages.filter { it.chatId == chatId }
    }
}