package com.app.co_opilot.service

import com.app.co_opilot.data.repository.ActivityRepository
import com.app.co_opilot.domain.Activity
import com.app.co_opilot.domain.enums.Sections
import java.util.Date

class ActivityService(
    private val activityRepository: ActivityRepository
) {

    suspend fun getActivity(id: String): Activity? {
        return activityRepository.getActivity(id)
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
    ) {
        activityRepository.createActivity(
            id,
            ownerId,
            title,
            description,
            sections,
            createdAt,
            updatedAt,
            startAt,
            endAt,
            tags
        )
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
        return activityRepository.updateActivity(
            id,
            ownerId,
            title,
            description,
            sections,
            startAt,
            endAt,
            tags
        )
    }

    suspend fun deleteActivity(id: String): Boolean {
        return activityRepository.deleteActivity(id)
    }

    suspend fun getAllActivity(): List<Activity> {
        return activityRepository.getAllActivity()
    }

    suspend fun getAllActivityForUser(userId: String): List<Activity> {
        return getAllActivity().filter { activity -> activity.ownerId == userId }
    }
}