package com.app.co_opilot.domain.profile

import kotlinx.serialization.Serializable

// https://git.uwaterloo.ca/b5wu/co-opilot/-/work_items/20
@Serializable
data class CareerProfile (
    val industry : String ?= null,
    val yearsOfExp: Int? = null,
    val pastInternships: List<String> = emptyList(),
    val skills: List<String> = emptyList()
)