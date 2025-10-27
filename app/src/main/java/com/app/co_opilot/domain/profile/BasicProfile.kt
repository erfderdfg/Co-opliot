package com.app.co_opilot.domain.profile

import kotlinx.serialization.Serializable

// https://git.uwaterloo.ca/b5wu/co-opilot/-/work_items/20
@Serializable
enum class Gender { FEMALE, MALE, NON_BINARY, OTHER, UNSPECIFIED }
@Serializable
data class BasicProfile(
    val gender: Gender = Gender.UNSPECIFIED,
    val religion: String? = null,
    val nationality: String? = null,
    val dateOfBirth: String? = null
)