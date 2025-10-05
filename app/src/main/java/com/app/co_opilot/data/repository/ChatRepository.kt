package com.app.co_opilot.data.repository

import com.app.co_opilot.domain.Chat
import com.app.co_opilot.domain.Message
import com.app.co_opilot.domain.User

class ChatRepository {
    suspend fun getChatSession(chatId: String): Chat {
        // TODO: dummy data: modify when connecting to Supabase
        return Chat(chatId, "user1", "user2");
    }

    suspend fun getChatHistory(chatId: String): List<Message> {
        // TODO: dummy data: modify when connecting to Supabase
        return listOf(Message("mid", "cid", "Hello!", "user1", "2025-10-05"));
    }

    suspend fun getChat(userOneId: String, userTwoId: String): Chat {
        return Chat("id1", userOneId, userTwoId);
    }

    suspend fun sendMessage(message: Message) {
        return;
    }

}