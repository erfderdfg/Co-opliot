package com.app.co_opilot.domain

import com.app.co_opilot.domain.enums.RelationshipStatus

data class Relationship(
    val id: String,                    // Unique identifier for the relationship
    val userOneId: String,             // First user's ID
    val userTwoId: String,             // Second user's ID
    val status: RelationshipStatus,    // Current relationship status
    val createdAt: String,             // Timestamp when created
    val updatedAt: String? = null      // Optional: last updated timestamp
)
