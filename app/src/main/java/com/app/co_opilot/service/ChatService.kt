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
    private suspend fun haveMutualLike(userOneId: String, userTwoId: String): Boolean {
        if (relationshipRepo == null) return true

        val userOneLikedUserTwo = relationshipRepo.findRelationship(userOneId, userTwoId)
            ?.status == RelationshipStatus.LIKED

        val userTwoLikedUserOne = relationshipRepo.findRelationship(userTwoId, userOneId)
            ?.status == RelationshipStatus.LIKED

        return userOneLikedUserTwo && userTwoLikedUserOne
    }

    suspend fun sendMessage(senderId: String, receiverId: String, message: String): Boolean {

        try {
            /*if (!haveMutualLike(senderId, receiverId)) {
                println("Cannot send message: Users have not mutually liked each other")
                return false
            }*/

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

    suspend fun initSession(senderId: String, receiverId: String) {
        /*if (!haveMutualLike(senderId, receiverId)) {
            throw IllegalStateException("Cannot create chat: Users have not mutually liked each other")
        }*/
        chatRepo.initSession(senderId, receiverId)
    }


    suspend fun loadChatHistory(userOneId: String, userTwoId: String): List<Message> {
        try {
            /*if (!haveMutualLike(userOneId, userTwoId)) {
                return emptyList()
            }*/

            val chat = try {
                chatRepo.getChat(userOneId, userTwoId)
            } catch (e: Exception) {
                chatRepo.initSession(userOneId, userTwoId)
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

            if (relationshipRepo == null) {
                return allChats
            }

            val mutualChats = allChats.filter { chat ->
                val otherUserId = if (chat.userOneId == userId) chat.userTwoId else chat.userOneId
                /*haveMutualLike(userId, otherUserId) && */ !isEitherUserBlocked(userId, otherUserId)
            }

            mutualChats
        } catch (ce: CancellationException) {
            throw ce
        } catch (_: Exception) {
            emptyList()
        }
    }

    private suspend fun isEitherUserBlocked(userOneId: String, userTwoId: String): Boolean {
        if (relationshipRepo == null) return false

        val rel1 = relationshipRepo.findRelationship(userOneId, userTwoId)
        val rel2 = relationshipRepo.findRelationship(userTwoId, userOneId)

        return rel1?.status == RelationshipStatus.BLOCKED ||
                rel2?.status == RelationshipStatus.BLOCKED
    }

    suspend fun blockUserAndDeleteChat(blockerId: String, blockedId: String): Boolean {
        return try {
            chatRepo.deleteChat(blockerId, blockedId)

            relationshipRepo?.blockUser(blockerId, blockedId) ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}