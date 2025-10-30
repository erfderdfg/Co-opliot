package com.app.co_opilot.service

import com.app.co_opilot.data.repository.ChatRepository
import com.app.co_opilot.data.repository.RelationshipRepository
import com.app.co_opilot.data.repository.UserRepository
import com.app.co_opilot.domain.Chat
import com.app.co_opilot.domain.Message
import com.app.co_opilot.domain.User

class ChatService(
    private val chatRepo: ChatRepository,
    private val relationshipRepo: RelationshipRepository? = null,
    private val userRepo: UserRepository? = null
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

}