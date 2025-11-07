package com.app.co_opilot.data.repository

import com.app.co_opilot.data.provider.SupabaseProvider
import com.app.co_opilot.domain.Chat
import com.app.co_opilot.domain.Message
import io.github.jan.supabase.postgrest.postgrest
import java.util.*

open class ChatRepository(val supabase : SupabaseProvider) {

    suspend fun initSession(userOneId: String, userTwoId: String): Chat {
        return try {
            val existing = supabase.client.postgrest["chats"]
                .select {
                    filter {
                        "user_one_id" to userOneId
                        "user_two_id" to userTwoId
                    }
                }
                .decodeList<Chat>()
                .firstOrNull()

            if (existing != null) existing


            val newChat = Chat(
                id = UUID.randomUUID().toString(),
                userOneId = userOneId,
                userTwoId = userTwoId,
                createdAt = Date().toInstant().toString()
            )
            supabase.client.postgrest["chats"].insert(listOf(newChat))
            newChat
        } catch (e: Exception) {
            e.printStackTrace()
            throw IllegalStateException("Failed to initialize chat session", e)
        }
    }

    suspend fun getChat(userOneId: String, userTwoId: String): Chat {
        return try {
            supabase.client.postgrest["chats"]
                .select {
                    filter {
                        or {
                            and {
                                eq("user_one_id", userOneId)
                                eq("user_two_id", userTwoId)
                            }
                            and {
                                eq("user_one_id", userTwoId)
                                eq("user_two_id", userOneId)
                            }
                        }
                    }
                }
                .decodeSingle<Chat>()
        } catch (e: Exception) {
            e.printStackTrace()
            throw IllegalStateException("Failed to initialize chat session", e)
        }
    }

    suspend fun getChats(userId: String): List<Chat> { // get list of chats associated with userId
//        println("we have " + userId)
        return try {
            supabase.client.postgrest["chats"]
                .select {
                    filter {
                        or {
                            eq("user_one_id", userId)
                            eq("user_two_id", userId)
                        }
                    }
                }
                .decodeList<Chat>()
        } catch (e: Exception) {
            e.printStackTrace()
            throw IllegalStateException("Failed to initialize chat session", e)
        }
    }

    suspend fun sendMessage(senderId: String, chatId: String, content: String): Boolean {
        return try {
            val message = Message(
                id = UUID.randomUUID().toString(),
                chatId = chatId,
                senderId = senderId,
                message = content,
                sentAt = Date().toInstant().toString()
            )
            supabase.client.postgrest["messages"].insert(listOf(message))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getChatHistory(chatId: String): List<Message> {
        return try {
            supabase.client.postgrest["messages"]
                .select {
                    filter {
                        eq("chat_id", chatId)
                    }
                }
                .decodeList<Message>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
