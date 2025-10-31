package com.app.co_opilot.data.repository

import com.app.co_opilot.data.SupabaseClient
import com.app.co_opilot.data.provider.SupabaseProvider
import com.app.co_opilot.domain.Activity
import com.app.co_opilot.domain.enums.Sections
import io.github.jan.supabase.postgrest.postgrest
import java.time.Instant
import java.util.*

open class ActivityRepository(val supabase: SupabaseProvider) {

    suspend fun getActivity(id: String): Activity? {
        return try {
            val activity = supabase.client.postgrest["activities"]
                .select {
                    filter {
                        eq("id", id)
                    }
                }
                .decodeSingle<Activity>()
            activity
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun createActivity(
        id: String,
        ownerId: String,
        title: String,
        description: String,
        sections: Sections,
        createdAt: Date,
        updatedAt: Date,
        startAt: Date,
        endAt: Date,
        tags: List<String>
    ): Boolean {
        return try {
            val activity = Activity(
                id = id,
                ownerId = ownerId,
                title = title,
                description = description,
                sections = sections,
                startAt = startAt.toInstant().toString(),
                endAt = endAt.toInstant().toString(),
                tags = tags,
                createdAt = createdAt.toInstant().toString(),
                updatedAt = updatedAt.toInstant().toString()
            )
            supabase.client.postgrest["activities"].insert(listOf(activity))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // ✅ Update an existing activity
    suspend fun updateActivity(
        id: String,
        ownerId: String? = null,
        title: String? = null,
        description: String? = null,
        sections: Sections? = null,
        startAt: Date? = null,
        endAt: Date? = null,
        tags: List<String>? = null
    ): Boolean {
        return try {
            val existing = getActivity(id) ?: return false
            val updated = existing.copy(
                ownerId = ownerId ?: existing.ownerId,
                title = title ?: existing.title,
                description = description ?: existing.description,
                sections = sections ?: existing.sections,
                startAt = startAt?.toInstant()?.toString() ?: existing.startAt,
                endAt = endAt?.toInstant()?.toString() ?: existing.endAt,
                tags = tags ?: existing.tags,
                updatedAt = Date().toInstant().toString()
            )
            supabase.client.postgrest["activities"].update(updated) {
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

    // ✅ Delete activity by ID
    suspend fun deleteActivity(id: String): Boolean {
        return try {
            supabase.client.postgrest["activities"].delete {
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

    // ✅ Get all activities
    suspend fun getAllActivity(): List<Activity> {
        return try {
            supabase.client.postgrest["activities"]
                .select()
                .decodeList<Activity>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
