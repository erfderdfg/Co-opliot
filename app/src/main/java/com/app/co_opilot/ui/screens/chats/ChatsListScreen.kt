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
import com.app.co_opilot.util.formatDateDisplay
import com.app.co_opilot.util.parseDate
import java.time.Instant
import java.util.Date

class ChatsListScreen: Screen {
    @Composable
    fun ChatCard(user2: User, lstMsg: Message?) {
        val userId = "id1";
        val lastSentAt = if (lstMsg != null) formatDateDisplay(parseDate(lstMsg.sentAt)) else ""
        val tabNavigator = LocalTabNavigator.current
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = CardDefaults.cardColors(),
            onClick = {
                // navigate to Chat Screen with specified target user2 id
                ChatScreen.NavStates.pendingChatSession.value = Pair(userId, user2.id)
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

        var friends by remember { mutableStateOf<List<User>>(emptyList()) }
        val lastMessage = remember { mutableStateMapOf<String, Message>() } // <friend_id, last-msg>
        var isLoading by remember { mutableStateOf(true) }

        val userId = "id1"; // replace this with actual supabase session

        LaunchedEffect(userId) {
            isLoading = true
//            friends = viewModel.chatService.loadFriends(userId) // suspend call to chatService
            friends = listOf(
                User(
                    "id",
                    "Tommy",
                    "tommy@gmail.com",
                    null,
                    Instant.now().toString(),
                    Instant.now().toString(),
                    BasicProfile(),
                    AcademicProfile(
                        faculty = "Math",
                        major = mutableListOf("Computer Science", "Pure Math"),
                        academicyear = 3,
                        school = "University of Waterloo",
                        gpa = 92.0
                    ),
                    CareerProfile(
                        skills = mutableListOf("Web Dev", "AI", "System Engineering"),
                        industry = "Software",
                        yearsOfExp = 1,
                        pastInternships = mutableListOf("Google", "Amazon", "Meta")
                    ),
                    SocialProfile(
                        linkedin_url = "https://www.linkedin.com/in/tommypang04/",
                        instagram_username = "tommypang04",
                        x_url = "https://x.com/elonmusk",
                        interests = mutableListOf("Sports", "Science", "Music"),
                        hobbies = mutableListOf("Hiking", "Badminton", "Guitar"),
                        mbti = "INTJ"
                    )
                ),
                User( // change this to actual supabase auth session user
                    "id2",
                    "Benny",
                    "benny@gmail.com",
                    null,
                    Instant.now().toString(),
                    Instant.now().toString(),
                    BasicProfile(),
                    AcademicProfile(
                        faculty = "Math",
                        major = mutableListOf("Computer Science", "Stat"),
                        academicyear = 3,
                        school = "University of Waterloo",
                        gpa = 92.0
                    ),
                    CareerProfile(
                        skills = mutableListOf("Web Dev", "AI", "System Engineering"),
                        industry = "Software",
                        yearsOfExp = 1,
                        pastInternships = mutableListOf("Google", "Amazon", "Meta")
                    ),
                    SocialProfile(
                        linkedin_url = "https://www.linkedin.com/in/bennywu/",
                        instagram_username = "bennywu",
                        x_url = "https://x.com/elonmusk",
                        interests = mutableListOf("Sports", "Science", "Music"),
                        hobbies = mutableListOf("Hiking", "Badminton", "Guitar"),
                        mbti = "INTJ"
                    )
                )
            )
            for (user2 in friends) {
//                val messages = viewModel.chatService.loadChatHistory(userId, user2.id) // load from actual supabase
                val messages = listOf(
                    Message( // change this to actual supabase auth session user
                        "id",
                        "chat_id",
                        "snd_id",
                        "Hello!",
                        Instant.now().toString()
                    ), Message( // change this to actual supabase auth session user
                        "id",
                        "chat_id",
                        "snd_id",
                        "Hello!",
                        Instant.now().toString()
                    )
                )
                messages.sortedBy { m -> parseDate(m.sentAt) }
                if (!messages.isEmpty()) lastMessage[user2.id] = messages.last()
            }
            isLoading = false
        }

        Column(modifier = Modifier.padding(horizontal = 32.dp)) {
            Text("My Chats", fontSize = 32.sp, modifier = Modifier.padding(top = 16.dp))
            Spacer(Modifier.height(12.dp))
            when {
                isLoading -> Text("Loading chats...")
                friends.isEmpty() -> Text("No friends yet")
                else -> LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    items(friends, key = { it.id }) { friend ->
                        ChatCard(friend, lastMessage[friend.id])
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