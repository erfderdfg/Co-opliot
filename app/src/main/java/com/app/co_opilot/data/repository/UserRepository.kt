package com.app.co_opilot.data.repository

import com.app.co_opilot.domain.User
import com.app.co_opilot.domain.profile.AcademicProfile
import com.app.co_opilot.domain.profile.BasicProfile
import com.app.co_opilot.domain.profile.CareerProfile
import com.app.co_opilot.domain.profile.SocialProfile
import java.io.File
import java.util.Date

// UserService owns it
open class UserRepository( /* Inject remote API (Supabase DB Client) service */ ) {
    private var users = mutableListOf<User>()
    suspend fun getUser(userId: String) : User? {
        // dummy data: modify when connecting to Supabase, maybe assign users to supabase result
        return users.find { user: User -> user.id == userId }
    }

    suspend fun createUser(id: String, name: String, email: String, avatar: String?, createdAt: Date, updatedAt: Date, basicProfile: BasicProfile, academicProfile: AcademicProfile, careerProfile: CareerProfile, socialProfile: SocialProfile) {
        users.add(User(id, name, email, avatar, createdAt, updatedAt, basicProfile, academicProfile, careerProfile, socialProfile))
    }

    private suspend fun updateUser(updatedUser: User) {
        val index = users.indexOfFirst { it.id == updatedUser.id }
        if (index != -1) {
            users[index] = updatedUser.copy()
        }
    }

    suspend fun updateUserProfiles(
        userId: String,
        basic: BasicProfile? = null,
        academic: AcademicProfile? = null,
        career: CareerProfile? = null,
        social: SocialProfile? = null
    ): Boolean {
        val user = users.find { it.id == userId } ?: return false
        val updatedUser = user.copy(
            basicProfile = basic ?: user.basicProfile,
            academicProfile = academic ?: user.academicProfile,
            careerProfile = career ?: user.careerProfile,
            socialProfile = social ?: user.socialProfile,
            updatedAt = Date()
        )
        updateUser(updatedUser)
        return true;
    }

    suspend fun deleteUser(userId: String): Boolean {
        return users.removeAll { it.id == userId }
    }

    suspend fun getAllUsers(): List<User> {
        // use Supabase instead
        return users.toList()
    }
}