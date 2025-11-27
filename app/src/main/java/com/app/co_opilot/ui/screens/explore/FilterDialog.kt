package com.app.co_opilot.ui.screens.explore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.app.co_opilot.domain.User
import com.app.co_opilot.domain.enums.Sections

@Composable
fun FilterDialog(
    section: Sections?,
    users: List<User>,
    currentFilters: Any,
    onDismiss: () -> Unit,
    onApply: (Any) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .padding(16.dp),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Filter ${section?.toString()?.lowercase()?.replaceFirstChar { it.uppercase() } ?: "Users"}",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(Modifier.height(16.dp))

                when (section) {
                    Sections.ACADEMICS -> {
                        AcademicFilterContent(
                            users = users,
                            currentFilters = currentFilters as AcademicFilters,
                            onFiltersChange = { onApply(it) }
                        )
                    }
                    Sections.COOP -> {
                        CareerFilterContent(
                            users = users,
                            currentFilters = currentFilters as CareerFilters,
                            onFiltersChange = { onApply(it) }
                        )
                    }
                    Sections.SOCIAL -> {
                        SocialFilterContent(
                            users = users,
                            currentFilters = currentFilters as SocialFilters,
                            onFiltersChange = { onApply(it) }
                        )
                    }
                    null -> {
                        SocialFilterContent(
                            users = users,
                            currentFilters = currentFilters as SocialFilters,
                            onFiltersChange = { onApply(it) }
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = onDismiss) {
                        Text("Apply")
                    }
                }
            }
        }
    }
}

@Composable
fun AcademicFilterContent(
    users: List<User>,
    currentFilters: AcademicFilters,
    onFiltersChange: (AcademicFilters) -> Unit
) {
    var filters by remember { mutableStateOf(currentFilters) }
    
    // Extract unique values from users
    val allFaculties = users.mapNotNull { it.academicProfile?.faculty }.distinct().sorted()
    val allMajors = users.flatMap { it.academicProfile?.major ?: emptyList() }.distinct().sorted()
    val allYears = users.mapNotNull { it.academicProfile?.academicyear }.distinct().sorted()
    val allSchools = users.mapNotNull { it.academicProfile?.school }.distinct().sorted()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        FilterSection(
            title = "Faculty",
            options = allFaculties,
            selected = filters.faculties,
            onToggle = { faculty ->
                filters = filters.copy(
                    faculties = if (faculty in filters.faculties) {
                        filters.faculties - faculty
                    } else {
                        filters.faculties + faculty
                    }
                )
                onFiltersChange(filters)
            }
        )

        Spacer(Modifier.height(16.dp))

        FilterSection(
            title = "Major",
            options = allMajors,
            selected = filters.majors,
            onToggle = { major ->
                filters = filters.copy(
                    majors = if (major in filters.majors) {
                        filters.majors - major
                    } else {
                        filters.majors + major
                    }
                )
                onFiltersChange(filters)
            }
        )

        Spacer(Modifier.height(16.dp))

        FilterSection(
            title = "Academic Year",
            options = allYears.map { it.toString() },
            selected = HashSet<String>(filters.academicYears.map { it.toString() }),
            onToggle = { yearStr ->
                val year = yearStr.toIntOrNull() ?: return@FilterSection
                filters = filters.copy(
                    academicYears = if (year in filters.academicYears) {
                        filters.academicYears - year
                    } else {
                        filters.academicYears + year
                    }
                )
                onFiltersChange(filters)
            }
        )

        Spacer(Modifier.height(16.dp))

        FilterSection(
            title = "School",
            options = allSchools,
            selected = filters.schools,
            onToggle = { school ->
                filters = filters.copy(
                    schools = if (school in filters.schools) {
                        filters.schools - school
                    } else {
                        filters.schools + school
                    }
                )
                onFiltersChange(filters)
            }
        )
    }
}

@Composable
fun CareerFilterContent(
    users: List<User>,
    currentFilters: CareerFilters,
    onFiltersChange: (CareerFilters) -> Unit
) {
    var filters by remember { mutableStateOf(currentFilters) }
    
    val allIndustries = users.mapNotNull { it.careerProfile?.industry }.distinct().sorted()
    val allSkills = users.flatMap { it.careerProfile?.skills ?: emptyList() }.distinct().sorted()
    val allInternships = users.flatMap { it.careerProfile?.pastInternships ?: emptyList() }.distinct().sorted()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        FilterSection(
            title = "Industry",
            options = allIndustries,
            selected = filters.industries,
            onToggle = { industry ->
                filters = filters.copy(
                    industries = if (industry in filters.industries) {
                        filters.industries - industry
                    } else {
                        filters.industries + industry
                    }
                )
                onFiltersChange(filters)
            }
        )

        Spacer(Modifier.height(16.dp))

        FilterSection(
            title = "Skills (at least 1 common)",
            options = allSkills,
            selected = filters.skills,
            onToggle = { skill ->
                filters = filters.copy(
                    skills = if (skill in filters.skills) {
                        filters.skills - skill
                    } else {
                        filters.skills + skill
                    }
                )
                onFiltersChange(filters)
            }
        )

        Spacer(Modifier.height(16.dp))

        FilterSection(
            title = "Past Internships (at least 1 common)",
            options = allInternships,
            selected = filters.pastInternships,
            onToggle = { internship ->
                filters = filters.copy(
                    pastInternships = if (internship in filters.pastInternships) {
                        filters.pastInternships - internship
                    } else {
                        filters.pastInternships + internship
                    }
                )
                onFiltersChange(filters)
            }
        )
    }
}

@Composable
fun SocialFilterContent(
    users: List<User>,
    currentFilters: SocialFilters,
    onFiltersChange: (SocialFilters) -> Unit
) {
    var filters by remember { mutableStateOf(currentFilters) }
    
    val allHobbies = users.flatMap { it.socialProfile.hobbies }.distinct().sorted()
    val allInterests = users.flatMap { it.socialProfile.interests }.distinct().sorted()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        FilterSection(
            title = "Hobbies (at least 1 common)",
            options = allHobbies,
            selected = filters.hobbies,
            onToggle = { hobby ->
                filters = filters.copy(
                    hobbies = if (hobby in filters.hobbies) {
                        filters.hobbies - hobby
                    } else {
                        filters.hobbies + hobby
                    }
                )
                onFiltersChange(filters)
            }
        )

        Spacer(Modifier.height(16.dp))

        FilterSection(
            title = "Interests (at least 1 common)",
            options = allInterests,
            selected = filters.interests,
            onToggle = { interest ->
                filters = filters.copy(
                    interests = if (interest in filters.interests) {
                        filters.interests - interest
                    } else {
                        filters.interests + interest
                    }
                )
                onFiltersChange(filters)
            }
        )
    }
}

@Composable
fun FilterSection(
    title: String,
    options: List<String>,
    selected: Set<String>,
    onToggle: (String) -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        if (options.isEmpty()) {
            Text(
                text = "No options available",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(options) { option ->
                    FilterChip(
                        selected = option in selected,
                        onClick = { onToggle(option) },
                        label = { Text(option) }
                    )
                }
            }
        }
    }
}

