package com.app.co_opilot.ui.screens.profile

import android.provider.ContactsContract.Profile
import androidx.compose.runtime.staticCompositionLocalOf
import com.app.co_opilot.service.UserService
import com.app.co_opilot.ui.screens.explore.ExploreViewModel

val LocalProfileViewModel = staticCompositionLocalOf<ProfileViewModel> {
    error("ProfileViewModel not provided")
}
class ProfileViewModel(val userService: UserService) {
}