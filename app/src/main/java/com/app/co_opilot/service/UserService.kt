package com.app.co_opilot.service

import com.app.co_opilot.data.repository.RelationshipRepository
import com.app.co_opilot.data.repository.UserRepository
import com.app.co_opilot.domain.Relationship
import com.app.co_opilot.domain.User
import com.app.co_opilot.domain.enums.RelationshipStatus
import com.app.co_opilot.domain.profile.AcademicProfile
import com.app.co_opilot.domain.profile.BasicProfile
import com.app.co_opilot.domain.profile.CareerProfile
import com.app.co_opilot.domain.profile.SocialProfile
import java.util.Date

class UserService(var userRepository: UserRepository, var relationshipRepository: RelationshipRepository) {
    suspend fun login(email: String, password: String) {
        // supabase client?
    }
    suspend fun signup(name: String, email: String) {
        // supabase client?
    }
    suspend fun getUser(userId: String) : User? {
        return userRepository.getUser(userId)
    }

    suspend fun getUserByEmail(email: String) : User? {
        return userRepository.getUserByEmail(email)
    }

    suspend fun createUser(id: String, name: String, email: String, avatar: String?, createdAt: Date, updatedAt: Date, basicProfile: BasicProfile, academicProfile: AcademicProfile, careerProfile: CareerProfile, socialProfile: SocialProfile) {
        userRepository.createUser(id, name, email, avatar, createdAt, updatedAt, basicProfile, academicProfile, careerProfile, socialProfile)
    }

    suspend fun updateUserProfiles(
        userId: String,
        basic: BasicProfile? = null,
        academic: AcademicProfile? = null,
        career: CareerProfile? = null,
        social: SocialProfile? = null
    ): Boolean {
        return userRepository.updateUserProfiles(userId, basic, academic, career, social)
    }

    suspend fun deleteUser(userId: String): Boolean {
        return userRepository.deleteUser(userId);
    }

    suspend fun getAllUsers(): List<User> {
        return userRepository.getAllUsers();
    }

    suspend fun likeUser(userOneId: String, userTwoId: String) {
        // assume only one relationship status (LIKED) for now
        if (relationshipRepository.findRelationship(userOneId, userTwoId) == null) {
            relationshipRepository.addRelationship(userOneId, userTwoId, RelationshipStatus.LIKED)
        }
    }
}