package com.app.co_opilot.data.repository

import android.util.Log
import com.app.co_opilot.data.SupabaseClient
import com.app.co_opilot.data.provider.SupabaseProvider
import com.app.co_opilot.domain.User
import com.app.co_opilot.domain.profile.AcademicProfile
import com.app.co_opilot.domain.profile.BasicProfile
import com.app.co_opilot.domain.profile.CareerProfile
import com.app.co_opilot.domain.profile.SocialProfile
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

@Serializable
data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val avatar_url: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val basic_profile: BasicProfile? = null,
    val academic_profile: AcademicProfile? = null,
    val career_profile: CareerProfile? = null,
    val social_profile: SocialProfile? = null
)
private const val TAG = "UserRepository"
open class UserRepository(val supabase : SupabaseProvider) {

    suspend fun getUser(userId: String): User? {
        return try {
            val result = supabase.client.postgrest["users"]
                .select {
                    filter {
                        eq("id", userId)
                    }
                }
                .decodeSingle<User>()
            result
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

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

        supabase.client.postgrest["users"].insert(user)
    }

    suspend fun updateUserProfiles(
        userId: String,
        basic: BasicProfile? = null,
        academic: AcademicProfile? = null,
        career: CareerProfile? = null,
        social: SocialProfile? = null
    ): Boolean {
        return try {
            val currentUser = getUser(userId) ?: return false

            val updatedUser = User(
                id = userId,
                name = currentUser.name,
                email = currentUser.email,
                avatarUrl = currentUser.avatarUrl,
                createdAt = currentUser.createdAt,
                updatedAt = Date().toInstant().toString(),
                basicProfile = basic ?: currentUser.basicProfile,
                academicProfile = academic ?: currentUser.academicProfile,
                careerProfile = career ?: currentUser.careerProfile,
                socialProfile = social ?: currentUser.socialProfile
            )

            supabase.client.postgrest["users"]
                .update(updatedUser) {
                    filter {
                        "id" to userId
                    }
                }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun updateUser(
        userId: String,
        name: String? = null,
        email: String? = null,
        avatar: String? = null
    ): Boolean {
        return try {
            val currentUser = getUser(userId) ?: return false

            val updatedUser = User(
                id = userId,
                name = name ?: currentUser.name,
                email = email ?: currentUser.email,
                avatarUrl = avatar ?: currentUser.avatarUrl,
                createdAt = currentUser.createdAt,
                updatedAt = Date().toInstant().toString(),
                basicProfile = currentUser.basicProfile,
                academicProfile = currentUser.academicProfile,
                careerProfile = currentUser.careerProfile,
                socialProfile = currentUser.socialProfile
            )

            supabase.client.postgrest["users"]
                .update(updatedUser) {
                    filter {
                        eq("id", userId)
                    }
                }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteUser(userId: String): Boolean {
        return try {
            supabase.client.postgrest["users"].delete {
                filter {
                    eq("id", userId)
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    suspend fun getAllUsers(): List<User> {
        return try {
            val results = supabase.client.postgrest["users"]
                .select()
                .decodeList<User>()

            results.map { it }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

//    private fun UserDto.toDomain(): User {
//        fun parseMaybeTimezoneAware(input: String?): Date? {
//            if (input == null) return null
//            // simple check: contains 'Z' or an offset like "+02:00" use Instant.parse
//            val hasZone = input.endsWith("Z", ignoreCase = true) || input.matches(".*[+-]\\d{2}:?\\d{2}\$".toRegex())
//            return if (hasZone) {
//                try { Date.from(Instant.parse(input)) } catch (e: Exception) { null }
//            } else {
//                parseTimestampNoZone(input, ZoneId.of("UTC"))
//            }
//        }
//
//        return User(
//            id = id,
//            name = name,
//            email = email,
//            avatarUrl = avatar_url,
//            createdAt = parseMaybeTimezoneAware(created_at) ?: Date(),
//            updatedAt = parseMaybeTimezoneAware(updated_at) ?: Date(),
//            basicProfile = basic_profile ?: BasicProfile(),
//            academicProfile = academic_profile ?: AcademicProfile(),
//            careerProfile = career_profile ?: CareerProfile(),
//            socialProfile = social_profile ?: SocialProfile()
//        )
//    }
//
//    fun parseTimestampNoZone(input: String, zone: ZoneId = ZoneId.systemDefault()): Date? {
//        return try {
//            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
//            val ldt = LocalDateTime.parse(input, formatter)
//            Date.from(ldt.atZone(zone).toInstant())
//        } catch (e: Exception) {
//            null
//        }
//    }
}