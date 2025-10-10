package com.app.co_opilot.domain.profile

// https://git.uwaterloo.ca/b5wu/co-opilot/-/work_items/20
data class SocialProfile (
    val mbti : String ? = null,
    val interests: List<String> = emptyList(),
    val hobbies: List<String> = emptyList(),
)