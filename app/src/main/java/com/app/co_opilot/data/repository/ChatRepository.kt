package com.app.co_opilot.data.repository

import com.app.co_opilot.domain.Chat

class ChatRepository {
    suspend fun getChat(): Chat {
        // dummy data: modify when connecting to Supabase
        return Chat("id2", "userId1", "userId2");
    }
}