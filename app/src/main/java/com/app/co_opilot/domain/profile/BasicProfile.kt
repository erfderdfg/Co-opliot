package com.app.co_opilot.domain.profile

// https://git.uwaterloo.ca/b5wu/co-opilot/-/work_items/20

enum class Gender { FEMALE, MALE, NON_BINARY, OTHER, UNSPECIFIED }
data class BasicProfile(
    val gender: Gender = Gender.UNSPECIFIED,
    val religion: String? = null,
    val nationality: String? = null,
    val dateOfBirth: String? = null
)