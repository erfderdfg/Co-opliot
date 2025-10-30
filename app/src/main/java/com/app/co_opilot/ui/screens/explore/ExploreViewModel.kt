package com.app.co_opilot.ui.screens.explore

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import com.app.co_opilot.domain.Activity
import com.app.co_opilot.domain.User
import com.app.co_opilot.domain.enums.Sections
import com.app.co_opilot.domain.profile.AcademicProfile
import com.app.co_opilot.domain.profile.BasicProfile
import com.app.co_opilot.domain.profile.CareerProfile
import com.app.co_opilot.domain.profile.SocialProfile
import com.app.co_opilot.service.ActivityService
import com.app.co_opilot.service.UserService
import java.util.Date

val LocalExploreViewModel = staticCompositionLocalOf<ExploreViewModel> {
    error("ExploreViewModel not provided")
}

data class ExploreViewModel(val userService: UserService, val activityService: ActivityService) {
    var userIndex by mutableIntStateOf(0)
        private set
    val userList: List<User> = listOf(
        User( // change this to actual supabase auth session user
            "id",
            "Tommy",
            "tommy@gmail.com",
            null,
            Date().toString(),
            Date().toString(),
            BasicProfile(),
            AcademicProfile(
                faculty = "Math",
                major = mutableListOf("Computer Science", "Pure Math"),
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
                linkedin_url = "https://www.linkedin.com/in/tommypang04/",
                instagram_username = "tommypang04",
                x_url = "https://x.com/elonmusk",
                interests = mutableListOf("Sports", "Science", "Music"),
                hobbies = mutableListOf("Hiking", "Badminton", "Guitar"),
                mbti = "INTJ"
            )
        ),
        User( // change this to actual supabase auth session user
            "id",
            "Benny",
            "benny@gmail.com",
            null,
            Date().toString(),
            Date().toString(),
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
    )
    fun swipeToNextUser() {
        val size = userList.size
        userIndex = (userIndex + 1) % size
    }

    fun swipeToPreviousUser() {
        val size = userList.size
        userIndex = (userIndex - 1 + size) % size
    }
    fun getActivities(userId: String): MutableList<Activity> {
        return mutableListOf(Activity(
            id = "activity_id",
            ownerId = "id",
            description = "Get some bubble tea at the alley together",
            title = "Bubble tea",
            sections = Sections.SOCIAL,
            createdAt = Date(),
            updatedAt = Date(),
            startAt = Date(),
            endAt = Date(),
            tags = mutableListOf("#social", "#alley", "#meetup")
        ), Activity(
            id = "activity_id2",
            ownerId = "id",
            description = "Get some bubble tea at the alley together",
            title = "Bubble tea",
            sections = Sections.SOCIAL,
            createdAt = Date(),
            updatedAt = Date(),
            startAt = Date(),
            endAt = Date(),
            tags = mutableListOf("#social", "#alley", "#meetup")
        ))
    }
}