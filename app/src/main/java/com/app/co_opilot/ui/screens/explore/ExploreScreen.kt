package com.app.co_opilot.ui.screens.explore

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Stars
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.app.co_opilot.domain.enums.Sections
import com.app.co_opilot.ui.screens.discovery.DiscoveryScreen
import kotlinx.coroutines.flow.MutableStateFlow

class ExploreScreen(private val section: Sections?): Screen {
    object NavStates { // Channel between DiscoveryScreen and ExploreScreen to read section
        val pendingExploreSection = MutableStateFlow<Sections?>(null)
    }

    @Composable
    override fun Content() {
        // use global auth session/state, redirect to auth page if needed
        Text(
            text = "Explore Screen for: $section",
            fontSize = 30.sp
        )
    }
    object ExploreTab : Tab {

        @Composable
        override fun Content() {
            Navigator(ExploreScreen(section = null)) { navigator ->
                // manually navigate to target section selected
                val pending by NavStates.pendingExploreSection.collectAsState()

                LaunchedEffect(pending, navigator) {
                    pending?.let {
                        navigator.push(ExploreScreen(it))
                        NavStates.pendingExploreSection.value = null
                    }
                }

                CurrentScreen()
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