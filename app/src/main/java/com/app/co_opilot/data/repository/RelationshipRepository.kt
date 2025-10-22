package com.app.co_opilot.data.repository

import com.app.co_opilot.data.SupabaseClient
import com.app.co_opilot.domain.Relationship
import com.app.co_opilot.domain.enums.RelationshipStatus
import io.github.jan.supabase.postgrest.postgrest
import java.util.*


class RelationshipRepository {

    private val supabase = SupabaseClient.client

    suspend fun getAllRelationships(): List<Relationship> {
        return try {
            supabase.postgrest["relationships"].select().decodeList<Relationship>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getRelationshipsForUser(userId: String): List<Relationship> {
        return try {
            supabase.postgrest["relationships"]
                .select {
                    filter {
                        "user_one_id" to userId
                    }
                }
                .decodeList<Relationship>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun addRelationship(userOneId: String, userTwoId: String, status: RelationshipStatus): Boolean {
        return try {
            val relationship = Relationship(
                id = UUID.randomUUID().toString(),
                userOneId = userOneId,
                userTwoId = userTwoId,
                status = status,
                createdAt = Date().toInstant().toString(),
                updatedAt = Date().toInstant().toString()
            )
            supabase.postgrest["relationships"].insert(listOf(relationship))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun findRelationship(userOneId: String, userTwoId: String): Relationship? {
        return try {
            supabase.postgrest["relationships"]
                .select {
                    filter {
                        "user_one_id" to userOneId
                        "user_two_id" to userTwoId
                    }
                }
                .decodeList<Relationship>()
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
