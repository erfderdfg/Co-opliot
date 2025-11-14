package com.app.co_opilot.ui.screens.setting

import androidx.compose.runtime.staticCompositionLocalOf
import com.app.co_opilot.domain.Message
import com.app.co_opilot.domain.User
import com.app.co_opilot.domain.profile.AcademicProfile
import com.app.co_opilot.domain.profile.BasicProfile
import com.app.co_opilot.domain.profile.CareerProfile
import com.app.co_opilot.domain.profile.SocialProfile
import com.app.co_opilot.service.UserService
import com.app.co_opilot.ui.screens.chats.ChatViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.lastOrNull

val LocalSettingViewModel = staticCompositionLocalOf<SettingViewModel> {
    error("SettingViewModel not provided")
}

data class SettingViewModel(val userService: UserService) {

    val updatedBasicProfile = MutableStateFlow<BasicProfile?>(null)
    val updatedAcademicProfile = MutableStateFlow<AcademicProfile?>(null)
    val updatedCareerProfile = MutableStateFlow<CareerProfile?>(null)
    val updatedSocialProfile = MutableStateFlow<SocialProfile?>(null)

    suspend fun saveUserProfile(userId: String) {
        userService.updateUserProfiles(
            userId,
            updatedBasicProfile.value,
            updatedAcademicProfile.value,
            updatedCareerProfile.value,
            updatedSocialProfile.value
        )
    }
}