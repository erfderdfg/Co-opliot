package com.app.co_opilot.ui.screens.chats

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.app.co_opilot.di.ServiceLocator
import kotlinx.coroutines.flow.MutableStateFlow

class ChatScreen(private val userIdPair: Pair<String, String>?): Screen {
    object NavStates { // Channel between ChatsListScreen and ChatScreen to open up chat
        val pendingChatSession = MutableStateFlow<Pair<String, String>?>(null)
    }
    object ChatTab : Tab {

        @Composable
        override fun Content() {
            val chatViewModel = remember {
                ChatViewModel(
                    chatService = ServiceLocator.chatService,
                    userService = ServiceLocator.userService
                )
            }
            CompositionLocalProvider(LocalChatViewModel provides chatViewModel) {
                Navigator(ChatScreen(userIdPair = null)) { navigator ->
                    val pending by NavStates
                        .pendingChatSession
                        .collectAsState()

                    LaunchedEffect(pending, navigator) {
                        pending?.let {
                            navigator.push(ChatScreen(it))
                            NavStates.pendingChatSession.value = null
                        }
                    }
                    CurrentScreen()
                }
            }
        }

        override val options: TabOptions
            @Composable get() {
                return TabOptions(
                    index = 0u,
                    title = "Chat",
                    icon = null
                )
            }
    }

    @Composable
    override fun Content() {
        if (userIdPair != null) Text(text = "THIS IS THE CHAT SCREEN")
    }
}