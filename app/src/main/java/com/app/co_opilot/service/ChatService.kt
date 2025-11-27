package com.app.co_opilot.service

import com.app.co_opilot.data.repository.ChatRepository
import com.app.co_opilot.data.repository.RelationshipRepository
import com.app.co_opilot.data.repository.UserRepository
import com.app.co_opilot.domain.Chat
import com.app.co_opilot.domain.Message
import com.app.co_opilot.domain.User
import com.app.co_opilot.domain.enums.RelationshipStatus
import kotlinx.coroutines.CancellationException

class ChatService(
    private val chatRepo: ChatRepository,
    private val relationshipRepo: RelationshipRepository? = null,
    private val userRepo: UserRepository? = null
) {
    /**
     * Check if two users have mutually liked each other
     */
    private suspend fun haveMutualLike(userOneId: String, userTwoId: String): Boolean {
        if (relationshipRepo == null) return true // If no relationship repo, allow all chats

        // Check if user one liked user two
        val userOneLikedUserTwo = relationshipRepo.findRelationship(userOneId, userTwoId)
            ?.status == RelationshipStatus.LIKED

        // Check if user two liked user one
        val userTwoLikedUserOne = relationshipRepo.findRelationship(userTwoId, userOneId)
            ?.status == RelationshipStatus.LIKED

        return userOneLikedUserTwo && userTwoLikedUserOne
    }

    suspend fun sendMessage(senderId: String, receiverId: String, message: String): Boolean {

        try {
            // Check if both users have mutually liked each other before allowing message
            if (!haveMutualLike(senderId, receiverId)) {
                println("Cannot send message: Users have not mutually liked each other")
                return false
            }

            val chat = try {
                chatRepo.getChat(senderId, receiverId)
            } catch (e: IllegalArgumentException) {
                // Chat doesn't exist, create it only if mutual like exists (already checked above)
                chatRepo.initSession(senderId, receiverId)
            }
            return chatRepo.sendMessage(senderId, chat.id, message)
        } catch (ce: CancellationException) {
            throw ce
        }

    }

    suspend fun initSession(senderId: String, receiverId: String) {
        // Check if both users have mutually liked each other before creating chat
        if (!haveMutualLike(senderId, receiverId)) {
            throw IllegalStateException("Cannot create chat: Users have not mutually liked each other")
        }
        chatRepo.initSession(senderId, receiverId)
    }


    suspend fun loadChatHistory(userOneId: String, userTwoId: String): List<Message> {
        try {
            // Check if both users have mutually liked each other
            if (!haveMutualLike(userOneId, userTwoId)) {
                return emptyList() // Return empty list if not mutually liked
            }

            val chat = try {
                chatRepo.getChat(userOneId, userTwoId)
            } catch (e: IllegalArgumentException) {
                // Don't automatically create chat - only return empty if it doesn't exist
                return emptyList()
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
            val allChats = chatRepo.getChats(userId)

            // Filter chats to only show those where both users have mutually liked each other
            if (relationshipRepo == null) {
                return allChats
            }

            val mutualChats = allChats.filter { chat ->
                val otherUserId = if (chat.userOneId == userId) chat.userTwoId else chat.userOneId
                haveMutualLike(userId, otherUserId)
            }

            mutualChats
        } catch (ce: CancellationException) {
            throw ce
        } catch (_: Exception) {
            emptyList()
        }
    }
}