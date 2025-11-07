package com.app.co_opilot.ui.screens.chats

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.app.co_opilot.domain.Message
import com.app.co_opilot.service.ChatService
import com.app.co_opilot.service.UserService
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.Instant

val LocalChatViewModel = staticCompositionLocalOf<ChatViewModel> {
    error("ChatViewModel not provided")
}

data class ChatViewModel(val chatService: ChatService, val userService: UserService) {
    val messages = MutableStateFlow<List<Message>>(emptyList())

    suspend fun loadChat(curUser: String, otherUser: String) {
        messages.value = chatService.loadChatHistory(curUser, otherUser)
    }

    suspend fun sendMessage(curUser: String, otherUser: String, text: String) {
        val newMsg = Message(
            id = System.currentTimeMillis().toString(),
            senderId = curUser,
            message = text,
            chatId = "",
            sentAt = Instant.now().toString()
        )

        // local append
        messages.value += newMsg

        // send to backend
        chatService.sendMessage(curUser, otherUser, text)
    }
}