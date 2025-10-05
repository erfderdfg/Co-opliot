package com.app.co_opilot.domain

import com.app.co_opilot.domain.profile.AcademicProfile
import com.app.co_opilot.domain.profile.BasicProfile
import com.app.co_opilot.domain.profile.CareerProfile
import com.app.co_opilot.domain.profile.SocialProfile
import java.util.Date
import kotlinx.serialization.Serializable

//@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val avatarUrl: String?,
    val createdAt: Date,
    val updatedAt: Date,
    val basicProfile: BasicProfile,
    val academicProfile: AcademicProfile,
    val careerProfile: CareerProfile,
    val socialProfile: SocialProfile
)