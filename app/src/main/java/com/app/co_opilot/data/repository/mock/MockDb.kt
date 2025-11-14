package com.app.co_opilot.data.repository.mock

import com.app.co_opilot.domain.*


// Simple in-memory "tables"
class FakeUserTable {
    private val data = mutableListOf<User>()

    fun insert(user: User) {
        data.add(user)
    }

    fun insertAll(users: List<User>) {
        data.addAll(users)
    }

    fun findById(id: String): User? = data.firstOrNull { it.id == id }

    fun update(user: User) {
        val idx = data.indexOfFirst { it.id == user.id }
        if (idx != -1) data[idx] = user
    }

    fun deleteById(id: String): Boolean =
        data.removeIf { it.id == id }

    fun getAll(): List<User> = data.toList()
}

class FakeRelationshipTable {
    private val data = mutableListOf<Relationship>()

    fun insert(rel: Relationship) {
        data.add(rel)
    }

    fun insertAll(rels: List<Relationship>) {
        data.addAll(rels)
    }

    fun getAll(): List<Relationship> = data.toList()

    fun findByUser(userId: String): List<Relationship> =
        data.filter { it.userOneId == userId }

    fun findPair(userOneId: String, userTwoId: String): Relationship? =
        data.firstOrNull {
            it.userOneId == userOneId && it.userTwoId == userTwoId
        }

    fun deleteById(id: String): Boolean =
        data.removeIf { it.id == id }
}

class FakeMessageTable {
    private val data = mutableListOf<Message>()

    fun insert(message: Message) {
        data.add(message)
    }

    fun insertAll(messages: List<Message>) {
        data.addAll(messages)
    }

    fun findById(id: String): Message? =
        data.firstOrNull { it.id == id }

    fun findByChat(chatId: String): List<Message> =
        data.filter { it.chatId == chatId }
}

class FakeChatTable {
    private val data = mutableListOf<Chat>()

    fun insert(chat: Chat) {
        data.add(chat)
    }

    fun insertAll(chats: List<Chat>) {
        data.addAll(chats)
    }

    fun findByUsers(userOneId: String, userTwoId: String): Chat? =
        data.firstOrNull {
            (it.userOneId == userOneId && it.userTwoId == userTwoId) ||
                    (it.userOneId == userTwoId && it.userTwoId == userOneId)
        }

    fun findByUser(userId: String): List<Chat> =
        data.filter { it.userOneId == userId || it.userTwoId == userId }

    fun getAll(): List<Chat> = data.toList()
}

class FakeActivityTable {
    private val data = mutableListOf<Activity>()

    fun insert(activity: Activity) {
        data.add(activity)
    }

    fun insertAll(activities: List<Activity>) {
        data.addAll(activities)
    }

    fun findById(id: String): Activity? =
        data.firstOrNull { it.id == id }

    fun update(activity: Activity) {
        val idx = data.indexOfFirst { it.id == activity.id }
        if (idx != -1) data[idx] = activity
    }

    fun deleteById(id: String): Boolean =
        data.removeIf { it.id == id }

    fun getAll(): List<Activity> = data.toList()
}
