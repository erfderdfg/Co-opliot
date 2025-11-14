package com.app.co_opilot.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.outlined.KeyboardDoubleArrowRight
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.app.co_opilot.R
import com.app.co_opilot.data.SupabaseClient
import com.app.co_opilot.di.ServiceLocator
import com.app.co_opilot.domain.User
import com.app.co_opilot.domain.Activity
import com.app.co_opilot.domain.enums.Sections
import com.app.co_opilot.domain.enums.SocialMedias
import com.app.co_opilot.domain.profile.AcademicProfile
import com.app.co_opilot.domain.profile.BasicProfile
import com.app.co_opilot.domain.profile.CareerProfile
import com.app.co_opilot.domain.profile.SocialProfile
import com.app.co_opilot.ui.components.SocialIcon
import com.app.co_opilot.ui.components.UserDeck
import com.app.co_opilot.ui.screens.discovery.DiscoveryScreen
import com.app.co_opilot.ui.screens.explore.ExploreViewModel
import com.app.co_opilot.ui.screens.explore.LocalExploreViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import com.app.co_opilot.ui.screens.explore.ExploreScreen
import com.app.co_opilot.ui.screens.explore.ExploreScreen.NavStates
import com.app.co_opilot.ui.screens.setting.SettingScreen
import java.time.Instant

class ProfileScreen() : Screen {

    @Composable
    private fun ProfileContent(
        user: User,
    ) {
        val tabNavigator = LocalTabNavigator.current
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()) {

            item {
                Spacer(Modifier.height(12.dp))
                Image(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(64.dp).clip(CircleShape)
                )
            }

            item {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(12.dp))
            }

            item {
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

            item{
                Spacer(Modifier.height(16.dp))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp, horizontal = 20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp).clickable {
                        tabNavigator.current = SettingScreen.SettingTab
                    }) {
                        Text(
                            text = "Settings",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Black
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "Edit your profile",
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Black
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
            item{
                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp, horizontal = 20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp).clickable {
                        // todo: navigation
                    }) {
                        Text(
                            text = "Statistics",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Black
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "View your stats",
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Black
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
            item {
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = {
                        // todo:: supabase auth
                    },
                    modifier = Modifier
                        .width(128.dp)
                        .height(56.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Logout", fontSize = 16.sp)
                }
            }
        }

    }

    @Composable
    override fun Content() {
        val viewModel = LocalProfileViewModel.current
        var user by remember { mutableStateOf<User?>(null) }
        var isLoading by remember { mutableStateOf(true) }
        var error by remember { mutableStateOf<Throwable?>(null) }
        val userId = "03a7b29c-ad4a-423b-b412-95cce56ceb94" // TODO: changeit
        LaunchedEffect(userId) {
            isLoading = true
            error = null
            try {
                val fetchedUser = viewModel.userService.getUser(userId)
//                user = fetchedUser
                user = User( // change this to actual supabase auth session user
                    "id2",
                    "Benny",
                    "benny@gmail.com",
                    null,
                    Instant.now().toString(),
                    Instant.now().toString(),
                    BasicProfile(),
                    AcademicProfile(
                        faculty = "Math",
                        major = mutableListOf("Computer Science", "Stat"),
                        academicyear = 3,
                        school = "University of Waterloo",
                        gpa = 92.0
                    ),
                    CareerProfile(
                        skills = mutableListOf("Web Dev", "AI", "System Engineering"),
                        industry = "Software",
                        yearsOfExp = 1,
                        pastInternships = mutableListOf("Google", "Amazon", "Meta")
                    ),
                    SocialProfile(
                        linkedin_url = "https://www.linkedin.com/in/bennywu/",
                        instagram_username = "bennywu",
                        x_url = "https://x.com/elonmusk",
                        interests = mutableListOf("Sports", "Science", "Music"),
                        hobbies = mutableListOf("Hiking", "Badminton", "Guitar"),
                        mbti = "INTJ"
                    )
                )

            } catch (t: Throwable) {
                error = t
            } finally {
                isLoading = false
            }
        }

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                ) {
                    Text("加载失败了 :(", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Text(error?.localizedMessage ?: "未知错误")
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = {
                        // 重试：改变 key 以触发 LaunchedEffect
                        // 这里简单地把 userId 触发一次，或者你也可以用一个 retryKey 的 state
                        // 为简洁起见，直接重新设置 isLoading 即可（LaunchedEffect 不会触发），
                        // 若你希望真正重试，建议加一个 retryKey:
                    }) {
                        Text("重试")
                    }
                }
            }
            user != null -> {
                ProfileContent(
                    user = user!!
                )
            }
        }
    }

    object ProfileTab : Tab {
        // 这里给出一个 Tab 示例：展示当前用户的个人页。
        // 如果你需要展示“当前登录用户”，用你项目中的会话/鉴权服务取当前 userId 即可。
        // 例如：ServiceLocator.authService.currentUserId()
        @Composable
        override fun Content() {
            val profileVm = remember {
                ProfileViewModel(
                    userService = ServiceLocator.userService,
                )
            }

            CompositionLocalProvider(LocalProfileViewModel provides profileVm) {
                Navigator(ProfileScreen()) { navigator ->
                    CurrentScreen()
                }
            }
        }

        override val options: TabOptions
            @Composable get() {
                val icon = rememberVectorPainter(Icons.Rounded.Person)
                return TabOptions(
                    index = 0u,
                    title = "Profile",
                    icon = icon
                )
            }
    }
}
