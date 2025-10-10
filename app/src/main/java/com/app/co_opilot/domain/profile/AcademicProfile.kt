package com.app.co_opilot.domain.profile

// https://git.uwaterloo.ca/b5wu/co-opilot/-/work_items/20
data class AcademicProfile (
    val faculty : String ? = null,
    val major: List<String>? = null,
    val academicyear: Int? = null,
    val school: String? = null,
    val gpa: Double? = null
)