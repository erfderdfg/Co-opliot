package com.app.co_opilot.domain.enums

import com.app.co_opilot.R

enum class Sections(val value: String) {
    // These are the 3 built-in categories / sections
    ACADEMICS("academics"),
    COOP("coop.png"),
    SOCIAL("social");

    override fun toString(): String = value

    fun toImageResourceId(): Int = run {
        when (value) {
            "academics" -> R.drawable.academic
            "coop.png" -> R.drawable.coop
            "social" -> R.drawable.social
            else -> {
                println("should not happen")
                throw Exception(String.format("value %s does not match with a valid section", value))
            }
        }
    }
    // For future reference
    // Benny: If user wants to attach a tag to an activity, let's make them customizable
    // (like Instagram hashtags): #CS346, #LeetCode, #CocoStudySession
}