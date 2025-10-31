package com.app.co_opilot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.app.co_opilot.ui.screens.auth.AuthScreen
import com.app.co_opilot.ui.screens.chats.ChatsListScreen
import com.app.co_opilot.ui.screens.discovery.DiscoveryScreen
import com.app.co_opilot.ui.screens.profile.ProfileScreen
import com.app.co_opilot.ui.theme.CoopilotTheme

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
    CoopilotTheme {
        TabNavigator(AuthScreen.AuthTab) {
            Scaffold(
                bottomBar = {
                    NavigationBar {
                        TabItem(DiscoveryScreen.DiscoveryTab)
                        TabItem(ChatsListScreen.ChatsListTab)
                        TabItem(ProfileScreen.ProfileTab)
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