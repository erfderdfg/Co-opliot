package com.app.co_opilot.ui.screens.chats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

class ChatsListScreen: Screen {
    @Composable
    override fun Content() {
        // use global auth session/state, redirect to auth page if needed
        Column {
            Text(text = "TESTING Chats SCREEN", fontSize = 30.sp)
        }
    }

    object ChatsListTab : Tab {

        @Composable
        override fun Content() {
            Navigator(ChatsListScreen()) { CurrentScreen() }
        }

        override val options: TabOptions
            @Composable get() {
                val icon = rememberVectorPainter(Icons.Outlined.ChatBubble)
                return TabOptions(
                    index = 0u,
                    title = "Chat",
                    icon = icon
                )
            }
    }
}