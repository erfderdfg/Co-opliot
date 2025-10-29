package com.app.co_opilot.ui.screens.profile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

class ProfileScreen: Screen {
    @Composable
    override fun Content() {
        Text(text = "TESTING PROFILE SCREEN", fontSize = 30.sp)
    }
    object ProfileTab : Tab {

        @Composable
        override fun Content() {
            Navigator(ProfileScreen()) { CurrentScreen() }
        }

        override val options: TabOptions
            @Composable get() {
                val icon = rememberVectorPainter(Icons.Rounded.Person)
                return TabOptions(
                    index = 0u,
                    title = "Profile",
                    icon = icon
                )
            }
    }
}