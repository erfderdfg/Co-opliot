package com.app.co_opilot.data

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class AuthState {
    var isAuthenticated by mutableStateOf(false)
}

val LocalAuthState = compositionLocalOf<AuthState> {
    error("No AuthState provided")
}