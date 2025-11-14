package com.app.co_opilot.ui.screens.explore

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import com.app.co_opilot.domain.Activity
import com.app.co_opilot.domain.User
import com.app.co_opilot.domain.enums.Sections
import com.app.co_opilot.domain.profile.AcademicProfile
import com.app.co_opilot.domain.profile.BasicProfile
import com.app.co_opilot.domain.profile.CareerProfile
import com.app.co_opilot.domain.profile.SocialProfile
import com.app.co_opilot.service.ActivityService
import com.app.co_opilot.service.ChatService
import com.app.co_opilot.service.MatchService
import com.app.co_opilot.service.UserService
import java.time.Instant
import java.util.Date

val LocalExploreViewModel = staticCompositionLocalOf<ExploreViewModel> {
    error("ExploreViewModel not provided")
}

data class ExploreViewModel(val matchService: MatchService,
                            val activityService: ActivityService,
                            val userService: UserService,
                            val chatService: ChatService) {
    val curUserId = "03a7b29c-ad4a-423b-b412-95cce56ceb94" // TODO: changeit

    var userIndex by mutableIntStateOf(0)
        private set

    var userList by mutableStateOf<List<User>>(emptyList())
        private set

    suspend fun loadUsers(section: Sections?) {
        userList = matchService.getRecommendations(curUserId, section)
    }

    fun swipeToNextUser() {
        val size = userList.size
        userIndex = (userIndex + 1) % size
    }

    fun swipeToPreviousUser() {
        val size = userList.size
        userIndex = (userIndex - 1 + size) % size
    }

    suspend fun likeUser(target: String) {
        userService.likeUser(curUserId, target)
        chatService.initSession(curUserId, target)
    }
}