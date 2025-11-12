package com.app.co_opilot.ui.screens.explore

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import com.app.co_opilot.ui.components.UserDeck
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

class ExploreScreen(private val section: Sections?): Screen {
    object NavStates {
        val pendingExploreSection = MutableStateFlow<Sections?>(null)
    }

    @Composable
    fun SwipingView(exploreViewModel: ExploreViewModel) {
        val allUsers = exploreViewModel.userList
        val currentUser = allUsers[exploreViewModel.userIndex]
        var activityCache by remember { mutableStateOf<Map<String, List<Activity>>>(emptyMap()) }

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

        val swipeThreshold = 300f // Distance needed to trigger swipe

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            UserDeck(
                section = section!!,
                user = currentUser,
                activities = activities,
                onMessageClick = {},
                modifier = Modifier
                    .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                    .graphicsLayer {
                        rotationZ = rotation.value
                        alpha = 1f - (offsetX.value.absoluteValue / 1000f).coerceIn(0f, 0.3f)
                    }
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                scope.launch {
                                    when {
                                        offsetX.value > swipeThreshold -> {
                                            // Swipe right - next user
                                            offsetX.animateTo(
                                                targetValue = 1500f,
                                                animationSpec = tween(300)
                                            )
                                            exploreViewModel.swipeToNextUser()
                                            offsetX.snapTo(0f)
                                            rotation.snapTo(0f)
                                        }
                                        offsetX.value < -swipeThreshold -> {
                                            // Swipe left - previous user
                                            offsetX.animateTo(
                                                targetValue = -1500f,
                                                animationSpec = tween(300)
                                            )
                                            exploreViewModel.swipeToPreviousUser()
                                            offsetX.snapTo(0f)
                                            rotation.snapTo(0f)
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
                                    offsetX.snapTo((offsetX.value + dragAmount).coerceIn(-800f, 800f))
                                    rotation.snapTo((offsetX.value / 20f).coerceIn(-15f, 15f))
                                }
                            }
                        )
                    }
            )
        }
    }

    @Composable
    override fun Content() {
        val viewModel = LocalExploreViewModel.current
        LaunchedEffect(Unit) {
            viewModel.loadUsers()
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
                    activityService = ServiceLocator.activityService
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