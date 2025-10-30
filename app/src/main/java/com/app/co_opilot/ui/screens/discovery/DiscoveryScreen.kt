package com.app.co_opilot.ui.screens.discovery

import android.widget.Space
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Stars
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import com.app.co_opilot.ui.theme.CoopilotTheme
import com.app.co_opilot.ui.components.ScreenHeader
import com.app.co_opilot.ui.screens.leaderboard.LeaderboardScreen

class DiscoveryScreen: Screen {
    @Composable
    fun SectionCard(title: String, description: String, sections: Sections) {
        val imageResId = sections.toImageResourceId();
        val tabNavigator = LocalTabNavigator.current

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .height(190.dp)
                .clip(RoundedCornerShape(12.dp))
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
                modifier = Modifier
                    .fillMaxWidth()
            )

            androidx.compose.foundation.layout.Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .background(MaterialTheme.colorScheme.primary)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onPrimary)
                    Text(description, fontSize = 13.sp, color = MaterialTheme.colorScheme.onPrimary)
                }
                Image(
                    painter = rememberVectorPainter(Icons.Outlined.ChevronRight),
                    contentDescription = null
                )
            }
        }
    }

    @Composable
    fun LeadershipCard() {
        val navigator = cafe.adriel.voyager.navigator.LocalNavigator.currentOrThrow
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .clip(RoundedCornerShape(12.dp))
                .clipToBounds()
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                .padding(14.dp)
                .clickable{ navigator.push(LeaderboardScreen()) }

        ) {
            androidx.compose.foundation.layout.Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Leaderboard", fontSize = 20.sp)
                    Text("View the global leaderboard", fontSize = 12.sp)
                }
                Image(
                    painter = rememberVectorPainter(Icons.Outlined.ChevronRight),
                    contentDescription = null
                )
            }
        }
    }

    @Composable
    override fun Content() {
        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp, vertical = 40.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            ScreenHeader(
                title = "Discovery",
                subtitle = "Find someone to share the grind with.",
                modifier = Modifier
            )

            LeadershipCard()

            SectionCard("Co-op Buddies", "Find matches to achieve career goals together", Sections.COOP)

            SectionCard("Academic Buddies", "Find matches to study together", Sections.ACADEMICS)

            SectionCard("Social Buddies", "Find matches to social & hangout", Sections.SOCIAL)

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


