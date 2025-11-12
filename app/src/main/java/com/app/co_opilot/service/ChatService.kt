package com.app.co_opilot.service

import com.app.co_opilot.data.repository.ChatRepository
import com.app.co_opilot.data.repository.RelationshipRepository
import com.app.co_opilot.data.repository.UserRepository
import com.app.co_opilot.domain.Chat
import com.app.co_opilot.domain.Message
import com.app.co_opilot.domain.User
import kotlinx.coroutines.CancellationException

class ChatService(
    private val chatRepo: ChatRepository,
    private val relationshipRepo: RelationshipRepository? = null,
    private val userRepo: UserRepository? = null
) {

    suspend fun sendMessage(senderId: String, receiverId: String, message: String): Boolean {

        try {
            val chat = try {
                chatRepo.getChat(senderId, receiverId)
            } catch (e: IllegalArgumentException) {
                chatRepo.initSession(senderId, receiverId)
            }
            return chatRepo.sendMessage(senderId, chat.id, message)
        } catch (ce: CancellationException) {
            throw ce
        }

    }


    suspend fun loadChatHistory(userOneId: String, userTwoId: String): List<Message> {
        try {
            val chat = try {
                chatRepo.getChat(userOneId, userTwoId)
            } catch (e: IllegalArgumentException) {
                chatRepo.initSession(userOneId, userTwoId)
            }
            return chatRepo.getChatHistory(chat.id)
        } catch (ce: CancellationException) {
            throw ce
        }
    }

    suspend fun loadFriends(userId: String): List<User> {
        val relRepo = relationshipRepo ?: return emptyList()
        val uRepo = userRepo ?: return emptyList()
        val relations = relRepo.getRelationships(userId)
        val friendIds = relations.map { it.userTwoId }
        val friends = mutableListOf<User>()
        for (fid in friendIds) {
            val u = uRepo.getUser(fid)
            if (u != null) friends.add(u)
        }
        return friends
    }

    suspend fun loadChats(userId: String): List<Chat> {
        return try {
            chatRepo.getChats(userId)
        } catch (ce: CancellationException) {
            throw ce
        } catch (_: Exception) {
            emptyList()
        }
    }
}