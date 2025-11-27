package com.app.co_opilot.ui.screens.chats

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.co_opilot.domain.Chat
import com.app.co_opilot.domain.Message
import com.app.co_opilot.service.ChatService
import com.app.co_opilot.service.UserService
import com.app.co_opilot.util.parseDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.Instant

val LocalChatViewModel = staticCompositionLocalOf<ChatViewModel> {
    error("ChatViewModel not provided")
}

data class ListPopupEvent(val chat: Chat, val message: Message)

class ChatViewModel(val chatService: ChatService, val userService: UserService) : ViewModel() {
    // existing messages for a single chat
    val messages = MutableStateFlow<List<Message>>(emptyList())

    // existing chat sessions
    val chatLists = MutableStateFlow<List<Chat>>(emptyList())

    // last incoming msg per chat: map of chatId → last incoming message
    val incomingMessageMap = MutableStateFlow<Map<String, Message>>(emptyMap())

    // last message (incoming/outgoing) per chat, for list preview
    val lastMessageMap = MutableStateFlow<Map<String, Message>>(emptyMap())

    // Popup state for chat list
    val listPopupMessage = MutableSharedFlow<ListPopupEvent>(replay = 0,1)

    private var chatListJob: Job? = null
    private var chatMsgJob: Job? = null
    private var seededIncoming = false
    private var activeChat: Pair<String,String>? = null

    fun startChatListPolling(curUser: String) {
        chatListJob?.cancel()
        chatListJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                val chats = chatService.loadChats(curUser)

                val latestMap = mutableMapOf<String, Message>()      // last msg (any direction)
                val latestIncoming = mutableMapOf<String, Message>()  // last incoming-only msg

                for (chat in chats) {
                    val history = chatService
                        .loadChatHistory(chat.userOneId, chat.userTwoId)
                        .sortedBy { parseDate(it.sentAt) }

                    val last = history.lastOrNull() ?: continue
                    latestMap[chat.id] = last
                    if (last.senderId != curUser) {
                        latestIncoming[chat.id] = last
                    }
                }

                chatLists.value = chats.sortedByDescending { c ->
                    latestMap[c.id]?.let { parseDate(it.sentAt).time } ?: Long.MIN_VALUE
                }
                lastMessageMap.value = latestMap

                // --- CORE FIX ---
                val previousIncoming = incomingMessageMap.value
                if (!seededIncoming) {
                    // First pass: seed baseline; DO NOT popup
                    incomingMessageMap.value = latestIncoming
                    seededIncoming = true
                } else {
                    // Compare against previous; popup only if new incoming appears
                    latestIncoming.forEach { (chatId, lastIncomingMsg) ->
                        val prev = previousIncoming[chatId]
                        if (prev == null || prev.id != lastIncomingMsg.id) {
                            // OPTIONAL: suppress popup for currently open chat
                            val isActiveChat = activeChat?.let { (u1, u2) ->
                                // We only know user ids; find the chat’s user pair
                                val chat = chats.firstOrNull { it.id == chatId }
                                chat != null && (
                                        (chat.userOneId == u1 && chat.userTwoId == u2) ||
                                                (chat.userOneId == u2 && chat.userTwoId == u1)
                                        )
                            } ?: false

                            if (!isActiveChat) {
                                listPopupMessage.tryEmit(ListPopupEvent(chats.first { it.id == chatId }, lastIncomingMsg))
                            }
                        }
                    }
                    // Always update the snapshot so future diffs are correct
                    incomingMessageMap.value = latestIncoming
                }
                // ---------------
                delay(5000)
            }
        }
    }

    fun loadChat(curUser: String, otherUser: String) {
        chatMsgJob?.cancel()
        messages.value = emptyList()

        chatMsgJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                val history = chatService
                    .loadChatHistory(curUser, otherUser)
                    .sortedBy { parseDate(it.sentAt) }
                messages.value = history
                delay(5000)
            }
        }
    }

    fun stopChatListPolling() {
        chatListJob?.cancel()
    }

    fun stopChatMsgPolling() {
        chatMsgJob?.cancel()
    }

    fun setActiveChat(userA: String, userB: String) { activeChat = userA to userB }
    fun clearActiveChat() { activeChat = null }

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

    suspend fun hasLikedUser(currentUserId: String, otherUserId: String): Boolean {
        return userService.hasLikedUser(currentUserId, otherUserId)
    }
}