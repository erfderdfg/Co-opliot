package com.app.co_opilot.data.repository

import android.service.voice.VoiceInteractionSession.ActivityId
import com.app.co_opilot.domain.Activity
import com.app.co_opilot.domain.enums.Sections
import java.util.Date

open class ActivityRepository {
    private var activities = mutableListOf<Activity>()
    suspend fun getActivity(id: String) : Activity? {
        // dummy data: modify when connecting to Supabase, maybe assign users to supabase result
        return activities.find { activity: Activity -> activity.id == id}
    }

    suspend fun createActivity(id: String, ownerId: String, title: String, description: String, sections: Sections, createdAt: Date, updatedAt:Date, startAt: Date, endAt: Date, tags: List<String>) {
        activities.add(Activity(id, ownerId, title, description, sections, createdAt, updatedAt, startAt, endAt, tags))
    }

    private suspend fun updateActivity(updatedActivity: Activity) {
        val index = activities.indexOfFirst { it.id == updatedActivity.id }
        if (index != -1) {
            activities[index] = updatedActivity.copy()
        }
    }

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
        val activity = activities.find { it.id == id } ?: return false
        val updatedActivity = activity.copy(
            ownerId = ownerId ?: activity.ownerId,
            title = title ?: activity.title,
            description = description ?: activity.description,
            sections = sections ?: activity.sections,
            startAt = startAt ?: activity.startAt,
            endAt = endAt ?: activity.endAt,
            tags = tags ?: activity.tags,
            updatedAt = Date()
        )
        updateActivity(updatedActivity)
        return true;
    }


    suspend fun deleteActivity(id: String): Boolean {
        return activities.removeAll { it.id == id }
    }

    suspend fun getAllActivity(): List<Activity> {
        // use Supabase instead
        return activities.toList()
    }

    // suspend fun getAllActivityFromUser()
}