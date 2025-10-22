package com.app.co_opilot.data.repository

import com.app.co_opilot.data.SupabaseClient
import com.app.co_opilot.domain.Relationship
import com.app.co_opilot.domain.enums.RelationshipStatus
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class RelationshipDto(
    val id: String,
    val user_one_id: String,
    val user_two_id: String?,
    val status: String?,
    val created_at: String? = null
)

class RelationshipRepository {

    private val supabase = SupabaseClient.client

    suspend fun getAllRelationships(): List<RelationshipDto> {
        return try {
            supabase.postgrest["relationships"].select().decodeList<RelationshipDto>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getRelationshipsForUser(userId: String): List<RelationshipDto> {
        return try {
            supabase.postgrest["relationships"]
                .select {
                    filter {
                        "user_one_id" to userId
                    }
                }
                .decodeList<RelationshipDto>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun addRelationship(userOneId: String, userTwoId: String, status: RelationshipStatus): Boolean {
        return try {
            val dto = RelationshipDto(
                id = UUID.randomUUID().toString(),
                user_one_id = userOneId,
                user_two_id = userTwoId,
                status = status.name,
                created_at = Date().toInstant().toString()
            )
            supabase.postgrest["relationships"].insert(listOf(dto))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun findRelationship(userOneId: String, userTwoId: String): RelationshipDto? {
        return try {
            supabase.postgrest["relationships"]
                .select {
                    filter {
                        "user_one_id" to userOneId
                        "user_two_id" to userTwoId
                    }
                }
                .decodeList<RelationshipDto>()
                .firstOrNull()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun deleteRelationship(id: String): Boolean {
        return try {
            supabase.postgrest["users"].delete {
                filter {
                    eq("id", id)
                }
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
