package com.app.co_opilot.ui.screens.explore

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.outlined.KeyboardDoubleArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.app.co_opilot.data.SupabaseClient
import com.app.co_opilot.di.ServiceLocator
import com.app.co_opilot.domain.Activity
import com.app.co_opilot.domain.User
import com.app.co_opilot.domain.enums.Sections
import com.app.co_opilot.domain.profile.AcademicProfile
import com.app.co_opilot.domain.profile.BasicProfile
import com.app.co_opilot.domain.profile.CareerProfile
import com.app.co_opilot.domain.profile.SocialProfile
import com.app.co_opilot.ui.components.UserDeck
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Date
import kotlin.math.exp

class ExploreScreen(private val section: Sections?): Screen {
    object NavStates { // Channel between DiscoveryScreen and ExploreScreen to read section
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

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Outlined.KeyboardDoubleArrowLeft,
                contentDescription = "Previous",
                modifier = Modifier.size(32.dp).padding(start = 8.dp).clickable {
                    // move left by one index, if any (make it cyclic)
                    println("go to previous card")
                    exploreViewModel.swipeToPreviousUser()
                }
            )

            UserDeck(
                section!!,
                currentUser,
                activities = activities,
                onMessageClick = {},
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Outlined.KeyboardDoubleArrowRight,
                contentDescription = "Next",
                modifier = Modifier.size(32.dp).padding(end = 8.dp).clickable {
                    // move right by one index, if any (make it cyclic)
                    println("go to next card")
                    exploreViewModel.swipeToNextUser()
                },
            )
        }
    }

    @Composable
    override fun Content() {
        // use global auth session/state, redirect to auth page if needed
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