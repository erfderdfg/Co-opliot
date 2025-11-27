package com.app.co_opilot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.app.co_opilot.data.AuthState
import com.app.co_opilot.data.LocalAuthState
import com.app.co_opilot.ui.screens.auth.AuthScreen
import com.app.co_opilot.ui.screens.chats.ChatsListScreen
import com.app.co_opilot.ui.screens.discovery.DiscoveryScreen
import com.app.co_opilot.ui.screens.profile.ProfileScreen
import com.app.co_opilot.ui.theme.CoopilotTheme
import kotlinx.coroutines.delay

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
    val authState = remember { AuthState() }

    var showBottomBar by remember { mutableStateOf(authState.isAuthenticated) }

    LaunchedEffect(authState.isAuthenticated) {
        if (authState.isAuthenticated) {
            if (!showBottomBar) {
                delay(2000)
                showBottomBar = true
            }
        } else {
            showBottomBar = false
        }
    }
    CoopilotTheme {
        CompositionLocalProvider(LocalAuthState provides authState) {
            TabNavigator(AuthScreen.AuthTab) {
                val tabNavigator = LocalTabNavigator.current

                LaunchedEffect(authState.isAuthenticated) {
                    if (!authState.isAuthenticated) {
                        tabNavigator.current = AuthScreen.AuthTab
                    }
                }

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            NavigationBar {
                                TabItem(DiscoveryScreen.DiscoveryTab)
                                TabItem(ChatsListScreen.ChatsListTab)
                                TabItem(ProfileScreen.ProfileTab)
                            }
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
}

@Composable
private fun RowScope.TabItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    val authState = LocalAuthState.current
    NavigationBarItem(
        selected = tabNavigator.current == tab,
        onClick = {
            if (authState.isAuthenticated) {
                println("User is authenticated")
                tabNavigator.current = tab
            } else {
                println("No authenticated user found. Redirecting to AuthScreen.")
                tabNavigator.current = AuthScreen.AuthTab
            }
                  },
        icon = { tab.options.icon?.let { Icon(painter = it, contentDescription = null) } },
        label = { Text(tab.options.title) }
    )
}