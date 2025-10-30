package com.app.co_opilot.ui.screens.discovery

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Stars
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.app.co_opilot.R
import com.app.co_opilot.domain.enums.Sections
import com.app.co_opilot.ui.screens.auth.AuthScreen
import com.app.co_opilot.ui.screens.explore.ExploreScreen

class DiscoveryScreen: Screen {
    @Composable
    fun SectionCard(title: String, description: String, sections: Sections) {
        val imageResId = sections.toImageResourceId();
        val tabNavigator = LocalTabNavigator.current

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(170.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF6F1FA))
                .clickable {
                    ExploreScreen.NavStates.pendingExploreSection.value = sections
                    tabNavigator.current = ExploreScreen.ExploreTab
                }
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .background(Color.White.copy(alpha = 0.85f))
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(title, fontSize = 18.sp)
                Text(description, fontSize = 13.sp)
            }
        }
    }

    @Composable
    fun LeadershipCard() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF7F0FF))
                .padding(16.dp)
        ) {
            Column {
                Text("Leaderboard", fontSize = 20.sp)
                Text("View the global leaderboard", fontSize = 12.sp)
            }
        }
    }

    @Composable
    override fun Content() {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(text = "Discovery", fontSize = 32.sp, modifier = Modifier.padding(top = 16.dp))
            Text(text = "Find someone to share the grind with.", fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp, bottom = 20.dp))

            LeadershipCard()

            SectionCard("Co-op Buddies", "Find best matches to achieve career goals together", Sections.COOP)

            SectionCard("Academic Buddies", "Find best matches to study together", Sections.ACADEMICS)

            SectionCard("Social Buddies", "Find best matches to social & hangout", Sections.SOCIAL)

            Spacer(Modifier.height(60.dp))
        }
    }

    object DiscoveryTab : Tab {

        @Composable
        override fun Content() {
            Navigator(DiscoveryScreen()) { CurrentScreen() }
        }

        override val options: TabOptions
            @Composable get() {
                val icon = rememberVectorPainter(Icons.Outlined.Stars)
                return TabOptions(
                    index = 0u,
                    title = "Discovery",
                    icon = icon
                )
            }
    }
}


