package com.app.co_opilot.domain.enums

import com.app.co_opilot.R

enum class SocialMedias(val value: String) {
    // These are the 3 built-in categories / sections
    INSTAGRAM("instagram"),
    X("x"),
    LINKEDIN("linkedin");

    override fun toString(): String = value

    fun toImageResourceId(): Int = run {
        when (value) {
            "instagram" -> R.drawable.instagram_icon
            "x" -> R.drawable.x_icon
            "linkedin" -> R.drawable.linkedin_icon
            else -> {
                println("should not happen")
                throw Exception(String.format("value %s does not match with a valid social media", value))
            }
        }
    }
    // For future reference
    // Benny: If user wants to attach a tag to an activity, let's make them customizable
    // (like Instagram hashtags): #CS346, #LeetCode, #CocoStudySession
}