package com.app.co_opilot.data.repository.mock


import com.app.co_opilot.domain.User
import com.app.co_opilot.domain.profile.*
import java.util.Date

open class MockUserRepository(
    private val table: FakeUserTable = FakeUserTable()
) {

    suspend fun seed(users: List<User>) {
        table.insertAll(users)
    }

    suspend fun getUser(userId: String): User? =
        table.findById(userId)

    suspend fun createUser(
        id: String,
        name: String,
        email: String,
        avatarUrl: String?,
        createdAt: Date,
        updatedAt: Date,
        basicProfile: BasicProfile,
        academicProfile: AcademicProfile,
        careerProfile: CareerProfile,
        socialProfile: SocialProfile
    ) {
        val user = User(
            id = id,
            name = name,
            email = email,
            avatarUrl = avatarUrl,
            createdAt = createdAt.toString(),
            updatedAt = updatedAt.toString(),
            basicProfile = basicProfile,
            academicProfile = academicProfile,
            careerProfile = careerProfile,
            socialProfile = socialProfile
        )
        table.insert(user)
    }

    suspend fun updateUserProfiles(
        userId: String,
        basic: BasicProfile? = null,
        academic: AcademicProfile? = null,
        career: CareerProfile? = null,
        social: SocialProfile? = null
    ): Boolean {
        val current = table.findById(userId) ?: return false
        val updated = current.copy(
            updatedAt = Date().toInstant().toString(),
            basicProfile = basic ?: current.basicProfile,
            academicProfile = academic ?: current.academicProfile,
            careerProfile = career ?: current.careerProfile,
            socialProfile = social ?: current.socialProfile
        )
        table.update(updated)
        return true
    }

    suspend fun updateUser(
        userId: String,
        name: String? = null,
        email: String? = null,
        avatar: String? = null
    ): Boolean {
        val current = table.findById(userId) ?: return false
        val updated = current.copy(
            name = name ?: current.name,
            email = email ?: current.email,
            avatarUrl = avatar ?: current.avatarUrl,
            updatedAt = Date().toInstant().toString()
        )
        table.update(updated)
        return true
    }

    suspend fun deleteUser(userId: String): Boolean =
        table.deleteById(userId)

    suspend fun getAllUsers(): List<User> =
        table.getAll()
}
