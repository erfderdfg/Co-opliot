package com.app.co_opilot.domain.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// https://git.uwaterloo.ca/b5wu/co-opilot/-/work_items/20
@Serializable
data class CareerProfile (
    val industry : String ?= null,
    @SerialName("years_of_experience") val yearsOfExp: Int? = null,
    @SerialName("past_internships") val pastInternships: List<String> = emptyList(),
    val skills: List<String> = emptyList()
)