package com.app.co_opilot.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.BusinessCenter
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Stars
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material.icons.outlined.WorkHistory
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        modifier = Modifier.size(32.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = socialMediaType.value,
            modifier = Modifier.size(20.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun InfoRow(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun AcademicInfoSection(user: User) {
    val academicProfile = user.academicProfile

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        academicProfile.major?.takeIf { it.isNotEmpty() }?.let {
            InfoRow(
                icon = Icons.Outlined.School,
                text = it.joinToString(", ")
            )
        }

        academicProfile.academicyear?.let {
            InfoRow(
                icon = Icons.Outlined.Stars,
                text = "${it}A"
            )
        }

        academicProfile.faculty?.takeIf { it.isNotEmpty() }?.let {
            InfoRow(
                icon = Icons.Outlined.LocationOn,
                text = it
            )
        }
    }
}

@Composable
fun CareerInfoSection(user: User) {
    val careerProfile = user.careerProfile

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        careerProfile.industry?.let {
            InfoRow(
                icon = Icons.Outlined.BusinessCenter,
                text = it
            )
        }

        careerProfile.yearsOfExp?.let {
            InfoRow(
                icon = Icons.Outlined.WorkHistory,
                text = "$it years of experience"
            )
        }

        if (careerProfile.skills.isNotEmpty()) {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = "Skills",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(careerProfile.skills) { skill ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                skill,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        if (careerProfile.pastInternships.isNotEmpty()) {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = "Past Experience",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(8.dp))

                careerProfile.pastInternships.forEach { internship ->
                    Text(
                        text = internship,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(6.dp))
                }
            }
        }
    }
}

@Composable
fun SocialInfoSection(user: User) {
    val socialProfile = user.socialProfile

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        socialProfile.mbti?.let {
            InfoRow(
                icon = Icons.Outlined.Psychology,
                text = it
            )
        }

        if (socialProfile.hobbies.isNotEmpty()) {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = "Hobbies",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(socialProfile.hobbies) { hobby ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                hobby,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        if (socialProfile.interests.isNotEmpty()) {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = "Interests",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(socialProfile.interests) { interest ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                interest,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
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
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = activity.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            if (activity.description.isNotBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = activity.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun UserDeck(
    section: Sections,
    user: User,
    activities: List<Activity>,
    isLiked: Boolean,
    onMessageClick: (() -> Unit)? = null,
    onLikeClick: (() -> Unit)? = null,
    onBlockClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var isLiked by remember(user.id) { mutableStateOf(isLiked) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        LazyColumn(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header: Avatar, Name, and Social Icons
            item {
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.user),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                    )

                    Spacer(Modifier.width(16.dp))

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = user.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
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

                            if (!sp.instagram_username.isNullOrBlank()) {
                                SocialIcon(SocialMedias.INSTAGRAM, openInstagram)
                            }
                            if (!sp.x_url.isNullOrBlank()) {
                                SocialIcon(SocialMedias.X, openUrl)
                            }
                            if (!sp.linkedin_url.isNullOrBlank()) {
                                SocialIcon(SocialMedias.LINKEDIN, openUrl)
                            }
                        }
                    }

                    IconButton(
                        onClick = { onMessageClick?.invoke() },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.Filled.ChatBubble,
                            contentDescription = "Send Message",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Divider
            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )
            }

            // Profile info based on selected section
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
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MaterialTheme.colorScheme.outlineVariant)
                    )
                }

                item {
                    Text(
                        "I like...",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "My favourite corner of the campus is...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                items(activities, key = { it.id }) { act ->
                    ActivityCard(activity = act)
                }
            }

            item {
                Spacer(Modifier.height(8.dp))
                FilledIconButton(
                    onClick = {
                        isLiked = !isLiked
                        onLikeClick?.invoke()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = if (isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
                        contentColor = if (isLiked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Like",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            if (isLiked) "Liked" else "Like",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            if (onBlockClick != null) {
                item {
                    Spacer(Modifier.height(8.dp))
                    FilledIconButton(
                        onClick = {
                            onBlockClick.invoke()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Filled.Block,
                                contentDescription = "Block",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Block User",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}