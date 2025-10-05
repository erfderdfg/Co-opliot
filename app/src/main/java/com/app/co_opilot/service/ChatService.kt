package com.app.co_opilot.service

import com.app.co_opilot.data.repository.ChatRepository
import com.app.co_opilot.domain.Chat
import com.app.co_opilot.domain.Message

class ChatService(
    private val chatRepo: ChatRepository
) {

    suspend fun sendMessage(senderId: String, receiverId: String, message: String) {
        // find chat Id of user1 and user2
        val chat: Chat = chatRepo.getChat(senderId, receiverId);

        // send message of chat
        chatRepo.sendMessage(Message("mid", "cid", senderId, message, "2025-05-10"));
    }

    // loadChatHistory(user1, user2)
    // loadChatSessions(user)
}