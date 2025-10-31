package com.app.co_opilot.domain

import com.app.co_opilot.domain.enums.Sections
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

// Fields: id, ownerId, title, description, section (import from enum)
// tags: (ex. #cs360, #leetcode, #cocoStudySession)

@Serializable
data class Activity(
    val id: String,
    @SerialName("owner_id") val ownerId: String,
    val title: String,
    val description: String,
    val sections: Sections,
    @SerialName("start_at") val startAt: String,
    @SerialName("end_at") val endAt: String,
    val tags: List<String>,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String
)
