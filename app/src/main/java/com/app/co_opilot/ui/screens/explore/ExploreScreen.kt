package com.app.co_opilot.ui.screens.explore

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.app.co_opilot.di.ServiceLocator
import com.app.co_opilot.domain.Activity
import com.app.co_opilot.domain.enums.Sections
import com.app.co_opilot.ui.components.ScreenHeader
import com.app.co_opilot.ui.components.UserDeck
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

class ExploreScreen(private val section: Sections?): Screen {
    object NavStates {
        val pendingExploreSection = MutableStateFlow<Sections?>(null)
    }

    @Composable
    fun SwipingView(exploreViewModel: ExploreViewModel) {
        val allUsers = exploreViewModel.userList
        val currentIndex = exploreViewModel.userIndex
        val currentUser = allUsers[currentIndex]
        val liked by remember { mutableStateOf(BooleanArray(size=allUsers.size) { false }) }
        var activityCache by remember { mutableStateOf<Map<String, List<Activity>>>(emptyMap()) }
        var showEndMessage by remember { mutableStateOf(false) }
        var endMessageText by remember { mutableStateOf("") }


        val activities = activityCache[currentUser.id] ?: emptyList()

        LaunchedEffect(currentUser.id) {
            if (activityCache[currentUser.id] == null) {
                val fresh = exploreViewModel.activityService.getAllActivityForUser(currentUser.id)
                activityCache = activityCache + (currentUser.id to fresh)
            }
        }

        // Swipe gesture state
        val offsetX = remember { Animatable(0f) }
        val rotation = remember { Animatable(0f) }
        val scope = rememberCoroutineScope()

        val swipeThreshold = 300f
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            ScreenHeader(section.toString().lowercase().replaceFirstChar { it.uppercase() } + " Explore",
                subtitle = "Swipe to find your next " + section.toString().lowercase() + " buddies!" )

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center

            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 2.dp, vertical = 14.dp),
                ) {
                    UserDeck(
                        section = section!!,
                        user = currentUser,
                        activities = activities,
                        onMessageClick = {},
                        isLiked = liked[currentIndex],
                        onLikeClick = {
                            scope.launch {
                                liked[currentIndex] = !liked[currentIndex]
                                exploreViewModel.likeUser(currentUser.id)
                            }
                        },
                        onBlockClick = {
                            scope.launch {
                                exploreViewModel.blockUser(currentUser.id)
                                if (currentIndex < allUsers.size - 1) {
                                    exploreViewModel.swipeToNextUser()
                                } else if (currentIndex > 0) {
                                    exploreViewModel.swipeToPreviousUser()
                                }
                            }
                        },
                        modifier = Modifier
                            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                            .graphicsLayer {
                                rotationZ = rotation.value
                                alpha = 1f - (offsetX.value.absoluteValue / 1000f).coerceIn(0f, 0.3f)
                            }
                            .pointerInput(currentIndex, allUsers.size) {
                                detectHorizontalDragGestures(
                                    onDragEnd = {
                                        scope.launch {
                                            when {
                                                offsetX.value > swipeThreshold -> {
                                                    // Swipe right - previous user
                                                    if (currentIndex > 0) {
                                                        offsetX.animateTo(
                                                            targetValue = 1500f,
                                                            animationSpec = tween(300)
                                                        )
                                                        exploreViewModel.swipeToPreviousUser()
                                                        offsetX.snapTo(0f)
                                                        rotation.snapTo(0f)
                                                    } else {
                                                        // At the beginning
                                                        endMessageText = "You are at the beginning"
                                                        showEndMessage = true
                                                        offsetX.animateTo(
                                                            targetValue = 0f,
                                                            animationSpec = tween(300)
                                                        )
                                                        rotation.animateTo(
                                                            targetValue = 0f,
                                                            animationSpec = tween(300)
                                                        )
                                                    }
                                                }
                                                offsetX.value < -swipeThreshold -> {
                                                    // Swipe left - next user
                                                    if (currentIndex < allUsers.size - 1) {
                                                        offsetX.animateTo(
                                                            targetValue = -1500f,
                                                            animationSpec = tween(300)
                                                        )
                                                        exploreViewModel.swipeToNextUser()
                                                        offsetX.snapTo(0f)
                                                        rotation.snapTo(0f)
                                                    } else {
                                                        // At the end
                                                        endMessageText = "You are at the end"
                                                        showEndMessage = true
                                                        offsetX.animateTo(
                                                            targetValue = 0f,
                                                            animationSpec = tween(300)
                                                        )
                                                        rotation.animateTo(
                                                            targetValue = 0f,
                                                            animationSpec = tween(300)
                                                        )
                                                    }
                                                }
                                                else -> {
                                                    // Return to center
                                                    offsetX.animateTo(
                                                        targetValue = 0f,
                                                        animationSpec = tween(300)
                                                    )
                                                    rotation.animateTo(
                                                        targetValue = 0f,
                                                        animationSpec = tween(300)
                                                    )
                                                }
                                            }
                                        }
                                    },
                                    onHorizontalDrag = { change, dragAmount ->
                                        change.consume()
                                        scope.launch {
                                            // Limit drag based on position
                                            val canDragRight = currentIndex > 0
                                            val canDragLeft = currentIndex < allUsers.size - 1

                                            val newOffset = offsetX.value + dragAmount

                                            val constrainedOffset = when {
                                                newOffset > 0 && !canDragRight -> {
                                                    // At beginning, limit right drag
                                                    (newOffset * 0.2f).coerceAtMost(100f)
                                                }
                                                newOffset < 0 && !canDragLeft -> {
                                                    // At end, limit left drag
                                                    (newOffset * 0.2f).coerceAtLeast(-100f)
                                                }
                                                else -> {
                                                    newOffset.coerceIn(-800f, 800f)
                                                }
                                            }

                                            offsetX.snapTo(constrainedOffset)
                                            rotation.snapTo((constrainedOffset / 20f).coerceIn(-15f, 15f))
                                        }
                                    }
                                )
                            }
                    )
                }

                // End message snackbar
                if (showEndMessage) {
                    Snackbar(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp),
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ) {
                        Text(endMessageText)
                    }

                    LaunchedEffect(Unit) {
                        delay(2000)
                        showEndMessage = false
                    }
                }
            }
        }

    }

    @Composable
    override fun Content() {
        val viewModel = LocalExploreViewModel.current
        LaunchedEffect(Unit) {
            viewModel.loadUsers(section)
        }
        if (viewModel.userList.isNotEmpty() && section != null) {
            SwipingView(viewModel)
        }
    }

    object ExploreTab : Tab {

        @Composable
        override fun Content() {
            val exploreVm = remember {
                ExploreViewModel(
                    matchService = ServiceLocator.matchService,
                    activityService = ServiceLocator.activityService,
                    userService = ServiceLocator.userService,
                    chatService = ServiceLocator.chatService,
                    authViewModel = ServiceLocator.authViewModel
                )
            }

            CompositionLocalProvider(LocalExploreViewModel provides exploreVm) {
                Navigator(ExploreScreen(section = null)) { navigator ->
                    val pending by NavStates
                        .pendingExploreSection
                        .collectAsState()

                    LaunchedEffect(pending, navigator) {
                        pending?.let {
                            navigator.push(ExploreScreen(it))
                            NavStates.pendingExploreSection.value = null
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
                    title = "Explore",
                    icon = null
                )
            }
    }
}