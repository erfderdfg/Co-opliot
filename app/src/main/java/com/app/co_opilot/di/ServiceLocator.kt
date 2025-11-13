package com.app.co_opilot.di

import com.app.co_opilot.data.SupabaseClient
import com.app.co_opilot.data.repository.ActivityRepository
import com.app.co_opilot.data.repository.ChatRepository
import com.app.co_opilot.data.repository.RelationshipRepository
import com.app.co_opilot.data.repository.UserRepository
import com.app.co_opilot.service.ActivityService
import com.app.co_opilot.service.ChatService
import com.app.co_opilot.service.MatchService
import com.app.co_opilot.service.UserService
import com.app.co_opilot.ui.screens.auth.AuthViewModel
import com.app.co_opilot.ui.screens.explore.ExploreViewModel

object ServiceLocator {
    val userService by lazy {
        UserService(UserRepository(SupabaseClient))
    }

    val activityService by lazy {
        ActivityService(ActivityRepository(SupabaseClient))
    }

    val chatService by lazy {
        ChatService(ChatRepository(SupabaseClient))
    }

    val matchService by lazy {
        MatchService(UserRepository(SupabaseClient), RelationshipRepository(SupabaseClient))
    }

    val exploreViewModel: ExploreViewModel by lazy {
        ExploreViewModel(
            matchService = matchService,
            activityService = activityService,
            authViewModel = authViewModel
        )
    }

    val authViewModel: AuthViewModel by lazy {
        AuthViewModel(userService)
    }
}