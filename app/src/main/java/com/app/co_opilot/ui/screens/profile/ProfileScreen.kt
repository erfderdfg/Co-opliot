package com.app.co_opilot.ui.screens.profile

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen

class ProfileScreen: Screen {
    @Composable
    override fun Content() {
        Text(text = "TESTING PROFILE SCREEN", fontSize = 30.sp)
    }
}