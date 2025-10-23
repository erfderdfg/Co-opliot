package com.app.co_opilot.ui.screens.discovery

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen

class DiscoveryScreen: Screen {
    @Composable
    override fun Content() {
        Text(text = "TESTING DISCOVERY SCREEN", fontSize = 30.sp)
    }
}