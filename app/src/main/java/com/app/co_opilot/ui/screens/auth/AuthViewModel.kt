package com.app.co_opilot.ui.screens.auth

import com.app.co_opilot.data.SupabaseClient
import com.app.co_opilot.service.UserService
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel(private val userService: UserService) {
    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId = _currentUserId.asStateFlow()

    suspend fun authenticateAndLoadUser(email: String) {
        try {
            // After successful Supabase auth, get user by email
            val user = userService.getUserByEmail(email)
            _currentUserId.value = user?.id
        } catch (e: Exception) {
            println("Error loading user: ${e.message}")
        }
    }

    fun clearCurrentUser() {
        _currentUserId.value = null
    }

    suspend fun logout() {
        try {
            // Sign out from Supabase
            SupabaseClient.client.auth.signOut()
            // Clear current user
            clearCurrentUser()
            println("Successfully logged out")
        } catch (e: Exception) {
            println("Error logging out: ${e.message}")
            throw e
        }
    }
}
