package com.app.co_opilot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.app.co_opilot.ui.screens.auth.AuthScreen
import com.app.co_opilot.ui.screens.chats.ChatsListScreen
import com.app.co_opilot.ui.screens.home.HomeScreen
import com.app.co_opilot.ui.screens.profile.ProfileScreen

class CoopilotApplication : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}

@Composable
fun App() {
    MaterialTheme {
        TabNavigator(AuthTab) {
            Scaffold(
                bottomBar = {
                    NavigationBar {
                        TabItem(HomeTab)
                        TabItem(ChatTab)
                        TabItem(ProfileTab)
                        // Add more TabItems (e.g. Discovery Tab, Auth Tab, etc)
                    }
                }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    CurrentTab()
                }
            }
        }
    }
}

@Composable
private fun RowScope.TabItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    NavigationBarItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        icon = { tab.options.icon?.let { Icon(painter = it, contentDescription = null) } },
        label = { Text(tab.options.title) }
    )
}

object AuthTab : Tab {

    @Composable
    override fun Content() {
        Navigator(AuthScreen()) { CurrentScreen() }
    }

    override val options: TabOptions
        @Composable get() {
            val icon = rememberVectorPainter(Icons.Outlined.Home)
            return TabOptions(
                index = 0u,
                title = "Auth",
                icon = icon
            )
        }
}

object HomeTab : Tab {

    @Composable
    override fun Content() {
        Navigator(HomeScreen()) { CurrentScreen() }
    }

    override val options: TabOptions
        @Composable get() {
            val icon = rememberVectorPainter(Icons.Outlined.Home)
            return TabOptions(
                index = 0u,
                title = "Home",
                icon = icon
            )
        }
}

// chat
// profile


object ChatTab : Tab {

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

// Add more tabs that encapsulate different screens