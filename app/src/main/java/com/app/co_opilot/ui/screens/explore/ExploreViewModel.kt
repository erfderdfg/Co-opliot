package com.app.co_opilot.ui.screens.explore

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
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
import com.app.co_opilot.service.ChatService
import com.app.co_opilot.service.MatchService
import com.app.co_opilot.service.UserService
import com.app.co_opilot.ui.screens.auth.AuthViewModel
import kotlinx.coroutines.flow.StateFlow
import java.time.Instant
import java.util.Date

val LocalExploreViewModel = staticCompositionLocalOf<ExploreViewModel> {
    error("ExploreViewModel not provided")
}

data class ExploreViewModel(
    val matchService: MatchService,
    val activityService: ActivityService,
    val userService: UserService,
    val chatService: ChatService,
    private val authViewModel: AuthViewModel) {

    val curUserId: StateFlow<String?> = authViewModel.currentUserId

    var userIndex by mutableIntStateOf(0)
        private set

    var userList by mutableStateOf<List<User>>(emptyList())
        private set
    
    var allUsers by mutableStateOf<List<User>>(emptyList())
        private set
    
    var filters by mutableStateOf(ExploreFilters())
        private set

    suspend fun loadUsers(section: Sections?) {
        val curUserId = curUserId.value ?: return
        allUsers = matchService.getRecommendations(curUserId, section)
        applyFilters(section)
    }
    
    fun updateFilters(section: Sections?, newFilters: Any) {
        filters = when (section) {
            Sections.ACADEMICS -> filters.copy(academic = newFilters as AcademicFilters)
            Sections.COOP -> filters.copy(career = newFilters as CareerFilters)
            Sections.SOCIAL -> filters.copy(social = newFilters as SocialFilters)
            null -> filters.copy(social = newFilters as SocialFilters)
        }
        applyFilters(section)
    }
    
    private fun applyFilters(section: Sections?) {
        val sectionFilters = filters.getFiltersForSection(section)
        userList = when (section) {
            Sections.ACADEMICS -> filterAcademicUsers(allUsers, sectionFilters as AcademicFilters)
            Sections.COOP -> filterCareerUsers(allUsers, sectionFilters as CareerFilters)
            Sections.SOCIAL -> filterSocialUsers(allUsers, sectionFilters as SocialFilters)
            null -> filterSocialUsers(allUsers, sectionFilters as SocialFilters)
        }
        // Reset index when filters change
        if (userList.isNotEmpty()) {
            userIndex = 0
        }
    }
    
    private fun filterAcademicUsers(users: List<User>, filters: AcademicFilters): List<User> {
        if (filters.isEmpty()) return users
        
        return users.filter { user ->
            val profile = user.academicProfile ?: return@filter false
            
            // AND logic: all selected filters must match
            val facultyMatch = filters.faculties.isEmpty() || 
                (profile.faculty != null && profile.faculty in filters.faculties)
            
            val majorMatch = filters.majors.isEmpty() || 
                (profile.major != null && profile.major.any { it in filters.majors })
            
            val yearMatch = filters.academicYears.isEmpty() || 
                (profile.academicyear != null && profile.academicyear in filters.academicYears)
            
            val schoolMatch = filters.schools.isEmpty() || 
                (profile.school != null && profile.school in filters.schools)
            
            facultyMatch && majorMatch && yearMatch && schoolMatch
        }
    }
    
    private fun filterCareerUsers(users: List<User>, filters: CareerFilters): List<User> {
        if (filters.isEmpty()) return users
        
        return users.filter { user ->
            val profile = user.careerProfile ?: return@filter false
            
            val industryMatch = filters.industries.isEmpty() || 
                (profile.industry != null && profile.industry in filters.industries)
            
            // At least 1 common skill
            val skillMatch = filters.skills.isEmpty() || 
                profile.skills.intersect(filters.skills).isNotEmpty()
            
            // At least 1 common internship employer
            val internshipMatch = filters.pastInternships.isEmpty() || 
                profile.pastInternships.intersect(filters.pastInternships).isNotEmpty()
            
            industryMatch && skillMatch && internshipMatch
        }
    }
    
    private fun filterSocialUsers(users: List<User>, filters: SocialFilters): List<User> {
        if (filters.isEmpty()) return users
        
        return users.filter { user ->
            val profile = user.socialProfile
            
            // At least 1 common hobby
            val hobbyMatch = filters.hobbies.isEmpty() || 
                profile.hobbies.intersect(filters.hobbies).isNotEmpty()
            
            // At least 1 common interest
            val interestMatch = filters.interests.isEmpty() || 
                profile.interests.intersect(filters.interests).isNotEmpty()
            
            hobbyMatch && interestMatch
        }
    }

    fun swipeToNextUser() {
        val size = userList.size
        userIndex = (userIndex + 1) % size
    }

    fun swipeToPreviousUser() {
        val size = userList.size
        userIndex = (userIndex - 1 + size) % size
    }

    suspend fun likeUser(target: String) {
        val currentUserId = curUserId.value ?: return
        userService.likeUser(currentUserId, target)

        try {
            chatService.initSession(currentUserId, target)
            println("Chat session created: mutual match between $currentUserId and $target")
        } catch (e: IllegalStateException) {
            println("Chat not created yet: waiting for mutual like between $currentUserId and $target")
        }
    }

    suspend fun blockUser(target: String) {
        val currentUserId = curUserId.value ?: return

        chatService.blockUserAndDeleteChat(currentUserId, target)

        println("User $target blocked by $currentUserId and chat deleted")

        loadUsers(null)
    }
}