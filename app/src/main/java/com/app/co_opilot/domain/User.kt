package com.app.co_opilot.domain

import com.app.co_opilot.domain.profile.AcademicProfile
import com.app.co_opilot.domain.profile.BasicProfile
import com.app.co_opilot.domain.profile.CareerProfile
import com.app.co_opilot.domain.profile.SocialProfile
import kotlinx.serialization.SerialName
import java.util.Date
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    @SerialName("avatar_url") val avatarUrl: String?,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("basic_profile") val basicProfile: BasicProfile,
    @SerialName("academic_profile") val academicProfile: AcademicProfile,
    @SerialName("career_profile") val careerProfile: CareerProfile,
    @SerialName("social_profile") val socialProfile: SocialProfile
)