package com.app.co_opilot.data.repository

import com.app.co_opilot.domain.Message

class MessageRepository {
    suspend fun getMessage(): Message {
        // TODO: dummy data: modify when connecting to Supabase
        return Message("messageId1", "chatId1", "uid1", "Honk!", "2025-10-4");
    }

}