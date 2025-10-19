package com.app.co_opilot.domain

import com.app.co_opilot.domain.enums.Sections
import java.util.Date

// Fields: id, ownerId, title, description, section (import from enum)
// tags: (ex. #cs360, #leetcode, #cocoStudySession)

// think through this: is it the cleanest / easiest approach to store it as a list, or as another SQL table

// TODO: Alice
//
data class Activity(
    val id: String,
    val ownerId: String,
    val title: String,
    val description: String,
    val sections: Sections,
    val createdAt: Date,
    val updatedAt: Date,
    val startAt: Date,
    val endAt: Date,
    val tags: List<String>
)
