package com.app.co_opilot.data.repository

import com.app.co_opilot.data.provider.SupabaseProvider
import com.app.co_opilot.domain.Chat
import com.app.co_opilot.domain.Message
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.CancellationException
import java.util.*

open class ChatRepository(val supabase : SupabaseProvider) {

    suspend fun initSession(userOneId: String, userTwoId: String): Chat {
        try {
            // Look for an existing chat in either user order
            val existing = supabase.client.postgrest["chats"]
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
                .decodeList<Chat>()
                .firstOrNull()

            if (existing != null) return existing   // <-- actually return it

            val newChat = Chat(
                id = UUID.randomUUID().toString(),
                userOneId = userOneId,
                userTwoId = userTwoId,
                createdAt = Date().toInstant().toString()
            )
            supabase.client.postgrest["chats"].insert(listOf(newChat))
            return newChat
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: Exception) {
            e.printStackTrace()
            throw IllegalStateException("Failed to initialize chat session", e)
        }
    }

    suspend fun getChat(userOneId: String, userTwoId: String): Chat {
        try {
            val chat = supabase.client.postgrest["chats"]
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
                .decodeList<Chat>()
                .firstOrNull()

            return chat ?: throw IllegalArgumentException("Chat not found")
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: Exception) {
            e.printStackTrace()
            // true error (network/schema/etc). Keep as state error.
            throw IllegalStateException("Failed to fetch chat", e)
        }
    }

    suspend fun getChats(userId: String): List<Chat> {
        try {
            return supabase.client.postgrest["chats"]
                .select {
                    filter {
                        or {
                            eq("user_one_id", userId)
                            eq("user_two_id", userId)
                        }
                    }
                }
                .decodeList<Chat>()
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: Exception) {
            e.printStackTrace()
            // Prefer empty list over crashing the app
            return emptyList()
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
        try {
            return supabase.client.postgrest["messages"]
                .select {
                    filter { eq("chat_id", chatId) }
                }
                .decodeList<Message>()
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }
}
