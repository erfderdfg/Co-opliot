package com.app.co_opilot.ui.screens.chats

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.app.co_opilot.R
import com.app.co_opilot.di.ServiceLocator
import com.app.co_opilot.domain.Chat
import com.app.co_opilot.domain.Message
import com.app.co_opilot.domain.User
import com.app.co_opilot.domain.enums.SocialMedias
import com.app.co_opilot.domain.profile.AcademicProfile
import com.app.co_opilot.domain.profile.BasicProfile
import com.app.co_opilot.domain.profile.CareerProfile
import com.app.co_opilot.domain.profile.SocialProfile
import com.app.co_opilot.ui.components.ScreenHeader
import com.app.co_opilot.util.formatDateDisplay
import com.app.co_opilot.util.parseDate
import java.time.Instant
import java.util.Date

class ChatsListScreen: Screen {
    @Composable
    fun ChatCard(chat: Chat, lstMsg: Message?, chatViewModel: ChatViewModel) {
        val tabNavigator = LocalTabNavigator.current
        val userId = "03a7b29c-ad4a-423b-b412-95cce56ceb94" // TODO: changeit
        val lastSentAt = if (lstMsg != null) formatDateDisplay(parseDate(lstMsg.sentAt)) else ""

        val (user2, setUser2) = remember { mutableStateOf<User?>(null) }
        LaunchedEffect(userId) {
            setUser2(
                if (userId == chat.userOneId) chatViewModel.userService.getUser(chat.userTwoId)
                else chatViewModel.userService.getUser(chat.userOneId)
            )
        }
        if (user2 == null) return

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = CardDefaults.cardColors(),
            onClick = {
                // navigate to Chat Screen with specified target user2 id
                ChatScreen.NavStates.pendingChatSession.value = Pair(chat.userOneId, chat.userTwoId)
                tabNavigator.current = ChatScreen.ChatTab
            }
        ) {
            Row(modifier = Modifier.padding(12.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(64.dp).clip(CircleShape)
                )
                Spacer(Modifier.width(8.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        text = user2.name,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = lstMsg?.message ?: "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = lastSentAt,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }

    @Composable
    override fun Content() {
        // use global auth session/state, redirect to auth page if needed
        val viewModel = LocalChatViewModel.current

        var chats by remember { mutableStateOf<List<Chat>>(emptyList()) }
        val lastMessage = remember { mutableStateMapOf<String, Message>() } // <chat_id, last-msg>
        var isLoading by remember { mutableStateOf(true) }

        val userId = "03a7b29c-ad4a-423b-b412-95cce56ceb94" // TODO: changeit

        LaunchedEffect(userId) {
            isLoading = true
            chats = viewModel.chatService.loadChats(userId)
            for (chat in chats) {
                val messages = viewModel.chatService.loadChatHistory(chat.userOneId, chat.userTwoId) // load from actual supabase
                messages.sortedBy { m -> parseDate(m.sentAt) }
                if (messages.isNotEmpty()) lastMessage[chat.id] = messages.last()
            }
            isLoading = false
        }

        Column(modifier = Modifier.padding(horizontal = 32.dp)) {

            ScreenHeader("My Chats", "A list of matched users. Start a chat with them!")
            when {
                isLoading -> Text("Loading chats...")
                chats.isEmpty() -> Text("No chats yet")
                else -> LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    items(chats, key = { it.id }) { chat ->
                        ChatCard(chat, lastMessage[chat.id], viewModel)
                        HorizontalDivider(
                            thickness = 0.7.dp,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }
        }
    }

    object ChatsListTab : Tab {

        @Composable
        override fun Content() {
            val chatViewModel = remember {
                ChatViewModel(
                    chatService = ServiceLocator.chatService,
                    userService = ServiceLocator.userService
                )
            }
            CompositionLocalProvider(LocalChatViewModel provides chatViewModel) {
                Navigator(ChatsListScreen()) { CurrentScreen() }
            }
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