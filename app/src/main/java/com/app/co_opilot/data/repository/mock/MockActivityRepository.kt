package com.app.co_opilot.data.repository.mock


import com.app.co_opilot.domain.Activity
import com.app.co_opilot.domain.enums.Sections
import java.util.*

open class MockActivityRepository(
    private val table: FakeActivityTable = FakeActivityTable()
) {

    suspend fun seed(activities: List<Activity>) {
        table.insertAll(activities)
    }

    suspend fun getActivity(id: String): Activity? =
        table.findById(id)

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
        val act = Activity(
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
        table.insert(act)
        return true
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
        val existing = table.findById(id) ?: return false
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
        table.update(updated)
        return true
    }

    suspend fun deleteActivity(id: String): Boolean =
        table.deleteById(id)

    suspend fun getAllActivity(): List<Activity> =
        table.getAll()
}
