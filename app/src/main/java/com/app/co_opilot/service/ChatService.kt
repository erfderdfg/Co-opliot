package com.app.co_opilot.service

import com.app.co_opilot.data.repository.ChatRepository
import com.app.co_opilot.domain.Chat
import com.app.co_opilot.domain.Message

class ChatService(
    private val chatRepo: ChatRepository
) {

    suspend fun sendMessage(senderId: String, receiverId: String, message: String): Boolean {

        var chat: Chat = try {
            chatRepo.getChat(senderId, receiverId)
        }
        catch (e: IllegalArgumentException) {
            // create new chat session if it doesn't exist
            chatRepo.initSession(senderId, receiverId)
        }

        // execute send message
        return chatRepo.sendMessage(senderId, chat.id, message)

    }


    suspend fun loadChatHistory(userOneId: String, userTwoId: String): List<Message> {
        // Try to find chat or create it if not exists
        val chat: Chat = try {
            chatRepo.getChat(userOneId, userTwoId)
        } catch (e: IllegalArgumentException) {
            chatRepo.initSession(userOneId, userTwoId)
        }

        // Return all messages in this chat session
        return chatRepo.getChatHistory(chat.id)
    }

}