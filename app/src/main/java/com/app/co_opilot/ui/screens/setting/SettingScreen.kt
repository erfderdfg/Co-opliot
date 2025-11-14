package com.app.co_opilot.ui.screens.setting

import androidx.compose.foundation.focusable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.app.co_opilot.domain.User
import com.app.co_opilot.domain.profile.AcademicProfile
import com.app.co_opilot.domain.profile.BasicProfile
import com.app.co_opilot.domain.profile.CareerProfile
import com.app.co_opilot.domain.profile.SocialProfile
import com.app.co_opilot.ui.components.ScreenHeader
import com.app.co_opilot.ui.screens.chats.LocalChatViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.app.co_opilot.di.ServiceLocator
import com.app.co_opilot.domain.profile.*
import com.app.co_opilot.ui.components.InputField
import com.app.co_opilot.ui.components.ListInputField
import com.app.co_opilot.ui.screens.chats.ChatViewModel
import com.app.co_opilot.ui.screens.chats.ChatsListScreen
import com.app.co_opilot.ui.screens.discovery.DiscoveryScreen
import com.app.co_opilot.ui.screens.profile.ProfileScreen
import kotlinx.coroutines.launch

class SettingScreen: Screen {
    @Composable
    fun BasicProfileSection(basic: BasicProfile, onChange: (BasicProfile) -> Unit) {
        Column(Modifier.padding(16.dp)) {
            Text("Basic Profile", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            InputField("Religion", basic.religion) {
                onChange(basic.copy(religion = it))
            }
            InputField("Nationality", basic.nationality) {
                onChange(basic.copy(nationality = it))
            }
            InputField("Date of Birth", basic.dateOfBirth) {
                onChange(basic.copy(dateOfBirth = it))
            }
        }
    }

    @Composable
    fun AcademicProfileSection(academic: AcademicProfile, onChange: (AcademicProfile) -> Unit) {
        Column(Modifier.padding(16.dp)) {
            Text("Academic Profile", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            InputField("Faculty", academic.faculty) {
                onChange(academic.copy(faculty = it))
            }

            ListInputField(
                label = "Add Major",
                items = academic.major ?: emptyList(),
                onAdd = { new ->
                    val updated = (academic.major ?: emptyList()) + new
                    onChange(academic.copy(major = updated))
                },
                onRemove = { remove ->
                    onChange(academic.copy(major = academic.major?.filter { it != remove }))
                }
            )

            InputField("Academic Year", academic.academicyear?.toString()) {
                onChange(academic.copy(academicyear = it.toIntOrNull()))
            }

            InputField("School", academic.school) {
                onChange(academic.copy(school = it))
            }

            InputField("GPA", academic.gpa?.toString()) {
                onChange(academic.copy(gpa = it.toDoubleOrNull()))
            }
        }
    }

    @Composable
    fun CareerProfileSection(career: CareerProfile, onChange: (CareerProfile) -> Unit) {
        Column(Modifier.padding(16.dp)) {
            Text("Career Profile", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            InputField("Industry", career.industry) {
                onChange(career.copy(industry = it))
            }
            InputField("Years of Experience", career.yearsOfExp?.toString()) {
                onChange(career.copy(yearsOfExp = it.toIntOrNull()))
            }

            ListInputField(
                label = "Add Past Internship",
                items = career.pastInternships,
                onAdd = { new ->
                    onChange(career.copy(pastInternships = career.pastInternships + new))
                },
                onRemove = { remove ->
                    onChange(career.copy(pastInternships = career.pastInternships - remove))
                }
            )

            ListInputField(
                label = "Add Skill",
                items = career.skills,
                onAdd = { new ->
                    onChange(career.copy(skills = career.skills + new))
                },
                onRemove = { remove ->
                    onChange(career.copy(skills = career.skills - remove))
                }
            )
        }
    }

    @Composable
    fun SocialProfileSection(social: SocialProfile, onChange: (SocialProfile) -> Unit) {
        Column(Modifier.padding(16.dp)) {
            Text("Social Profile", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            InputField("MBTI", social.mbti) {
                onChange(social.copy(mbti = it))
            }

            ListInputField(
                label = "Add Interest",
                items = social.interests,
                onAdd = { new -> onChange(social.copy(interests = social.interests + new)) },
                onRemove = { remove -> onChange(social.copy(interests = social.interests - remove)) }
            )

            ListInputField(
                label = "Add Hobby",
                items = social.hobbies,
                onAdd = { new -> onChange(social.copy(hobbies = social.hobbies + new)) },
                onRemove = { remove -> onChange(social.copy(hobbies = social.hobbies - remove)) }
            )

            InputField("Instagram Username", social.instagram_username) {
                onChange(social.copy(instagram_username = it))
            }
            InputField("X URL", social.x_url) {
                onChange(social.copy(x_url = it))
            }
            InputField("LinkedIn URL", social.linkedin_url) {
                onChange(social.copy(linkedin_url = it))
            }
        }
    }

    @Composable
    override fun Content() {
        val userId = "03a7b29c-ad4a-423b-b412-95cce56ceb94" // TODO: changeit
        val viewModel = LocalSettingViewModel.current
        val scope = rememberCoroutineScope()
        var curUser by remember { mutableStateOf<User?>(null) }
        LaunchedEffect(userId) {
            curUser = viewModel.userService.getUser(userId)
        }
        if (curUser == null) return

        LazyColumn(
            modifier = Modifier.fillMaxSize().focusable(),
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = "Tailor your profile to maximize visibility",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }

            item {
                BasicProfileSection(curUser!!.basicProfile) { new ->
                    curUser = curUser!!.copy(basicProfile = new)
                    viewModel.updatedBasicProfile.value = new
                }
            }

            item {
                AcademicProfileSection(curUser!!.academicProfile) { new ->
                    curUser = curUser!!.copy(academicProfile = new)
                    viewModel.updatedAcademicProfile.value = new
                }
            }

            item {
                CareerProfileSection(curUser!!.careerProfile) { new ->
                    curUser = curUser!!.copy(careerProfile = new)
                    viewModel.updatedCareerProfile.value = new
                }
            }

            item {
                SocialProfileSection(curUser!!.socialProfile) { new ->
                    curUser = curUser!!.copy(socialProfile = new)
                    viewModel.updatedSocialProfile.value = new
                }
            }

            item {
                Button(
                    onClick = {
                        scope.launch {
                            viewModel.saveUserProfile(userId)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Save", fontSize = 16.sp)
                }
            }
        }
    }
    object SettingTab : Tab {
        @Composable
        override fun Content() {
            val settingViewModel = remember {
                SettingViewModel(
                    userService = ServiceLocator.userService
                )
            }
            CompositionLocalProvider(LocalSettingViewModel provides settingViewModel) {
                Navigator(SettingScreen()) { CurrentScreen() }
            }
        }

        override val options: TabOptions
            @Composable get() {
                return TabOptions(
                    index = 0u,
                    title = "Setting",
                    icon = null
                )
            }
    }
}