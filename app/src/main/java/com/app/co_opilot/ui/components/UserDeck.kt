package com.app.co_opilot.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.BusinessCenter
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material.icons.outlined.WorkHistory
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.app.co_opilot.R
import com.app.co_opilot.domain.Activity
import com.app.co_opilot.domain.User
import com.app.co_opilot.domain.enums.Sections
import com.app.co_opilot.domain.enums.SocialMedias

@Composable
fun SocialIcon(socialMediaType: SocialMedias, onOpenSocial: ((platform: String) -> Unit)) {
    val iconRes = socialMediaType.toImageResourceId()
    IconButton(
        onClick = { onOpenSocial(socialMediaType.value) },
        modifier = Modifier.size(28.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = socialMediaType.value,
            modifier = Modifier.size(24.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun InfoRow(
    icon: ImageVector,
    label: String? = null,
    text: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = label, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(10.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun AcademicInfoSection(user: User) {
    val academicProfile = user.academicProfile

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {

        academicProfile.gpa?.let {
            InfoRow(
                icon = Icons.Outlined.BarChart,
                label = "GPA",
                text = "GPA: $it"
            )
        }

        academicProfile.major?.takeIf { it.isNotEmpty() }?.let {
            InfoRow(
                icon = Icons.Outlined.School,
                label = "Major",
                text = it.joinToString(", ")
            )
        }

        academicProfile.faculty?.takeIf { it.isNotEmpty() }?.let {
            InfoRow(
                icon = Icons.Outlined.AccountBalance,
                label = "Faculty",
                text = "Faculty: " + academicProfile.faculty.toString()
            )
        }

        academicProfile.academicyear?.let {
            InfoRow(
                icon = Icons.Outlined.Timeline,
                label = "Year",
                text = "Year " + academicProfile.academicyear.toString()
            )
        }
    }
}

@Composable
fun CareerInfoSection(user: User) {
    val careerProfile = user.careerProfile

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {

        careerProfile.industry?.let {
            InfoRow(
                icon = Icons.Outlined.BusinessCenter,
                label = "Industry",
                text = "Industry: $it"
            )
        }

        careerProfile.yearsOfExp?.let {
            InfoRow(
                icon = Icons.Outlined.WorkHistory,
                label = "YOE",
                text = "Year of Experience: $it"
            )
        }

        Column {
            Text(
                text = "Skills",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(Modifier.height(6.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(careerProfile.skills) { skill ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        tonalElevation = 2.dp
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(skill, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }

        Column {
            Text(
                text = "Past Experience",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(Modifier.height(6.dp))

            careerProfile.pastInternships.forEach { internship ->
                Text(
                    text = internship,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun SocialInfoSection(user: User) {
    val socialProfile = user.socialProfile

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {

        socialProfile.mbti?.let {
            InfoRow(
                icon = Icons.Outlined.Psychology,
                label = "MBTI",
                text = "MBTI: $it"
            )
        }

        Column {
            Text(
                text = "Hobbies",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(Modifier.height(6.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(socialProfile.hobbies) { hobby ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        tonalElevation = 2.dp
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(hobby, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }

        Column {
            Text(
                text = "Interests",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(Modifier.height(6.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(socialProfile.interests) { interest ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        tonalElevation = 2.dp
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(interest, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActivityCard(activity: Activity) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = activity.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(6.dp))
            if (activity.description.isNotBlank()) {
                Text(
                    text = activity.description,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun UserDeck(
    section: Sections,
    user: User,
    activities: List<Activity>,
    onMessageClick: (() -> Unit)? = null,
    modifier: Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item { // avatar, name, social media icons + message icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.user),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(64.dp).clip(CircleShape)
                    )

                    Spacer(Modifier.width(12.dp).height(12.dp))

                    Column(Modifier.weight(1f)) {
                        Text(
                            text = user.name,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            val sp = user.socialProfile
                            val uriHandler = LocalUriHandler.current

                            val openUrl: (String?) -> Unit = { url ->
                                if (!url.isNullOrBlank()) uriHandler.openUri(url)
                            }

                            val openInstagram: (String?) -> Unit = { username ->
                                if (!username.isNullOrBlank()) {
                                    uriHandler.openUri("https://www.instagram.com/$username")
                                }
                            }

                            // suppose social profile contains url for linkedin + x, username for insta
                            if (!sp.instagram_username.isNullOrBlank()) SocialIcon(SocialMedias.INSTAGRAM, openInstagram)
                            if (!sp.x_url.isNullOrBlank()) SocialIcon(SocialMedias.X, openUrl)
                            if (!sp.linkedin_url.isNullOrBlank()) SocialIcon(SocialMedias.LINKEDIN, openUrl)
                        }
                    }

                    IconButton(
                        onClick = { onMessageClick?.invoke() },
                        modifier = Modifier.size(36.dp)
                    ) { Icon(Icons.Filled.ChatBubble, contentDescription = "Send Message") }
                }
            }

            // profile info based on selected
            when (section.value) {
                "academics" -> {
                    item {
                        AcademicInfoSection(user)
                    }
                }
                "coop" -> {
                    item {
                        CareerInfoSection(user)
                    }
                }
                "social" -> {
                    item {
                        SocialInfoSection(user)
                    }
                }
                else -> {
                    println("should not happen")
                    throw Exception(String.format("value %s does not match with a valid section", section.value))
                }
            }

            // Activities
            if (activities.isNotEmpty()) {
                item {
                    Text(
                        "Activities",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                items(activities, key = { it.id }) { act ->
                    ActivityCard(activity = act)
                }
            }
        }
    }
}