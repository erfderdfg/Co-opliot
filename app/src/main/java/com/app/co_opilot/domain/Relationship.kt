package com.app.co_opilot.domain

import com.app.co_opilot.domain.enums.RelationshipStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Relationship(
    val id: String,
    @SerialName("user_one_id") val userOneId: String,
    @SerialName("user_two_id") val userTwoId: String,
    val status: RelationshipStatus,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String? = null
)
