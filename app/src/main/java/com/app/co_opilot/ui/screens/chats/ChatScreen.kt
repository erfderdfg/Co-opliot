package com.app.co_opilot.ui.screens.chats

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import com.app.co_opilot.domain.Message
import com.app.co_opilot.domain.User
import com.app.co_opilot.domain.profile.AcademicProfile
import com.app.co_opilot.domain.profile.BasicProfile
import com.app.co_opilot.domain.profile.CareerProfile
import com.app.co_opilot.domain.profile.SocialProfile
import com.app.co_opilot.util.formatDateDisplay
import com.app.co_opilot.util.parseDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.Instant

@Composable
fun ChatInputBar(
    onSend: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(Color.White)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Add, contentDescription = null)

        Spacer(modifier = Modifier.width(8.dp))

        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text("Message") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            )
        )

        Icon(
            imageVector = Icons.Default.Send,
            contentDescription = null,
            modifier = Modifier.clickable {
                if (text.isNotBlank()) {
                    onSend(text)
                    text = ""
                }
            }
        )
    }
}

@Composable
fun MessageBubble(message: Message, isMe: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        if (!isMe) {
            Image(
                painter = painterResource(id = R.drawable.user),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(48.dp).clip(CircleShape)
            )
        }
        Spacer(Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(
                    if (isMe) Color(0xFF554D64)
                    else Color(0xFFEDE7F6)
                )
                .padding(12.dp)
        ) {
            Text(
                text = message.message,
                color = if (isMe) Color.White else Color.Black
            )
        }
    }
}

@Composable
fun ChatTopBar(
    otherUser: User,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
                .size(28.dp)
                .clickable { onBack() }
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = otherUser.name,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

fun shouldShowTimestamp(prev: Message?, current: Message): Boolean {
    if (prev == null) return true

    val prevTime = parseDate(prev.sentAt)
    val curTime = parseDate(current.sentAt)

    val diffMinutes = java.time.Duration.between(prevTime.toInstant(), curTime.toInstant()).toMinutes()
    return diffMinutes >= 5
}

@Composable
fun TimestampLabel(time: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = formatDateDisplay(parseDate(time)),
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}

@Composable
fun ChatWindow(
    messages: List<Message>,
    curUserId: String,
    otherUser: User,
    onSend: suspend (String) -> Unit
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val tabNavigator = LocalTabNavigator.current

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }

    val onBack: () -> Unit = {
        // move back to chat list screen
        tabNavigator.current = ChatsListScreen.ChatsListTab
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F2FA)) // Light purple similar to screenshot
    ) {
        ChatTopBar(otherUser, onBack)

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            state = listState
        ) {
            itemsIndexed(messages) { index, msg ->
                val prev = messages.getOrNull(index - 1)

                if (shouldShowTimestamp(prev, msg)) {
                    TimestampLabel(msg.sentAt)
                    Spacer(Modifier.height(6.dp))
                }

                MessageBubble(message = msg, isMe = msg.senderId == curUserId)
                Spacer(Modifier.height(8.dp))
            }
        }

        ChatInputBar { txt ->
            scope.launch { onSend(txt) } // launch the suspend lambda
        }
    }
}

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
        val viewModel = LocalChatViewModel.current

        // curUser & otherUser
        val (user1Id, user2Id) = userIdPair ?: return
        val curUserId = "03a7b29c-ad4a-423b-b412-95cce56ceb94" // TODO: changeit
        val otherUserId = if (user1Id == curUserId) user2Id else user1Id

        val messages by viewModel.messages.collectAsState()
        var otherUser by remember { mutableStateOf<User?>(null) }

        LaunchedEffect(userIdPair) {
            viewModel.loadChat(user1Id, user2Id)
            otherUser = viewModel.userService.getUser(otherUserId)
        }
        if (otherUser == null) {
            return
        }

        ChatWindow(
            messages = messages,
            curUserId = curUserId,
            onSend = { text -> viewModel.sendMessage(curUserId, otherUserId, text) },
            otherUser = otherUser!!
        )
    }
}