package com.app.co_opilot.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import com.app.co_opilot.AuthTab

class HomeScreen: Screen {
    @Composable
    override fun Content() {
        val tabNavigator = LocalTabNavigator.current;
        // use global auth session/state, redirect to auth page if needed
        Text(text = "TESTING HOME SCREEN", fontSize = 30.sp)
    }
}