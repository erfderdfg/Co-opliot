package com.app.co_opilot.data.repository

import com.app.co_opilot.data.dto.MessageDto
import com.app.co_opilot.data.provider.SupabaseProvider
import com.app.co_opilot.domain.Message
import io.github.jan.supabase.postgrest.postgrest
import java.util.*

open class MessageRepository(val supabase : SupabaseProvider) {

    suspend fun getMessage(messageId: String): Message? {
        return try {
            supabase.client.postgrest["messages"]
                .select {
                    filter {
                        "id" to messageId
                    }
                }
                .decodeSingle<Message>()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getMessagesByChat(chatId: String): List<Message> {
        return try {
            supabase.client.postgrest["messages"]
                .select {
                    filter {
                        "chat_id" to chatId
                    }
                }
                .decodeList<Message>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun sendMessage(chatId: String, senderId: String, text: String): Boolean {
        return try {
            val dto = MessageDto(
                id = UUID.randomUUID().toString(),
                chat_id = chatId,
                sender_id = senderId,
                message = text,
                sent_at = Date().toInstant().toString()
            )
            supabase.client.postgrest["messages"].insert(listOf(dto))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
