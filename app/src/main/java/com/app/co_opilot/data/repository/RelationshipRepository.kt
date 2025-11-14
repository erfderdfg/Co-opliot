package com.app.co_opilot.data.repository

import com.app.co_opilot.data.provider.SupabaseProvider
import com.app.co_opilot.domain.Relationship
import com.app.co_opilot.domain.enums.RelationshipStatus
import io.github.jan.supabase.postgrest.postgrest
import java.util.*


open class RelationshipRepository(val supabase : SupabaseProvider) {

    suspend fun getAllRelationships(): List<Relationship> {
        return try {
            supabase.client.postgrest["relationships"]
                .select()
                .decodeList<Relationship>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getRelationships(userId: String): List<Relationship> {
        return try {
            supabase.client.postgrest["relationships"]
                .select {
                    filter { eq("user_one_id", userId) }
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
            supabase.client.postgrest["relationships"].insert(listOf(relationship))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun findRelationship(userOneId: String, userTwoId: String): Relationship? {
        return try {
            supabase.client.postgrest["relationships"]
                .select {
                    filter {
                        eq("user_one_id", userOneId)
                        eq("user_two_id", userTwoId)
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
            supabase.client.postgrest["users"].delete {
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
