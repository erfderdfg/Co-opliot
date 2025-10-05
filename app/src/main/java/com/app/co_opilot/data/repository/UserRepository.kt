package com.app.co_opilot.data.repository

import com.app.co_opilot.domain.User
import java.util.Date

class UserRepository {
    private var users = mutableListOf(User("userId1", "Mr. Goose", "honk@uwaterloo.ca", "", ""));
    suspend fun getUser(userId: String) : User? {
        // dummy data: modify when connecting to Supabase
        return users.find { user: User -> user.id == userId };
    }

    suspend fun createUser(id: String, name: String, email: String, avatar: String, createdAt: Date) {
        users.add(User(id, name, email, avatar, createdAt.toString()))
    }

    suspend fun updateUser(updatedUser: User) {
        val index = users.indexOfFirst { it.id == updatedUser.id }
        if (index != -1) {
            users[index] = updatedUser.copy()
        }
    }

    suspend fun deleteUser(userId: String) {
        users.removeAll { it.id == userId }
    }

    suspend fun getAllUsers(): List<User> {
        return users.toList();
    }
}