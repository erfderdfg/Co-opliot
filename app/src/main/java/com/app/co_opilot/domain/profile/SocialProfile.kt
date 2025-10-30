package com.app.co_opilot.domain.profile

import kotlinx.serialization.Serializable

// https://git.uwaterloo.ca/b5wu/co-opilot/-/work_items/20
@Serializable
data class SocialProfile (
    val mbti : String ? = null,
    val interests: List<String> = emptyList(),
    val hobbies: List<String> = emptyList(),
    val instagram_username : String? = null,
    val x_url: String? = null,
    val linkedin_url : String? = null
)