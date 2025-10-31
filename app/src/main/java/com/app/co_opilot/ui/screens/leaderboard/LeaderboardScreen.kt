package com.app.co_opilot.ui.screens.leaderboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.app.co_opilot.ui.components.ScreenHeader

class LeaderboardScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Column(modifier = Modifier.padding(horizontal = 32.dp, vertical = 32.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = { navigator.pop() }) {
                    Icon(
                        painter = rememberVectorPainter(Icons.Outlined.ChevronLeft),
                        contentDescription = "Back"
                    )
                }

            }

            ScreenHeader(
                title = "Leaderboard",
                subtitle = "Connect with more people to move up the leaderboard!",
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(12.dp))

            LeaderboardList(
                entries = sampleEntries()
            )
        }
    }

    @Composable
    private fun LeaderboardList(entries: List<LeaderboardEntry>) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(entries) { index, entry ->
                LeaderboardItem(
                    rank = index + 1,
                    entry = entry
                )
            }
        }
    }

    @Composable
    private fun LeaderboardItem(rank: Int, entry: LeaderboardEntry) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
            ) {
                // Rank
                Text(
                    text = "#${rank}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(end = 16.dp)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = entry.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = entry.subtitle,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Text(
                    text = "${entry.matches} matches",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }

    private data class LeaderboardEntry(
        val name: String,
        val subtitle: String,
        val matches: Int
    )

    private fun sampleEntries(): List<LeaderboardEntry> = listOf(
        LeaderboardEntry(name = "Molly", subtitle = "3A Math", matches = 23),
        LeaderboardEntry(name = "Alice", subtitle = "2B Mechatronics", matches = 17),
        LeaderboardEntry(name = "Tommy", subtitle = "4B Geomatics", matches = 3),
        LeaderboardEntry(name = "Benny", subtitle = "1A GBDA", matches = 0),
        LeaderboardEntry(name = "...", subtitle = "...", matches = 0)
    )
}