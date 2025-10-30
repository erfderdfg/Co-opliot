package com.app.co_opilot.ui.screens.chats

import androidx.compose.runtime.staticCompositionLocalOf
import com.app.co_opilot.service.ChatService
import com.app.co_opilot.service.UserService

val LocalChatViewModel = staticCompositionLocalOf<ChatViewModel> {
    error("ChatViewModel not provided")
}

data class ChatViewModel(val chatService: ChatService, val userService: UserService) {

}