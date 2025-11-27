package com.app.co_opilot.ui.screens.explore

import com.app.co_opilot.domain.enums.Sections

/**
 * Filter data classes for different sections
 */

data class AcademicFilters(
    val faculties: Set<String> = emptySet(),
    val majors: Set<String> = emptySet(),
    val academicYears: Set<Int> = emptySet(),
    val schools: Set<String> = emptySet()
) {
    fun isEmpty(): Boolean {
        return faculties.isEmpty() && majors.isEmpty() && academicYears.isEmpty() && schools.isEmpty()
    }
}

data class CareerFilters(
    val industries: Set<String> = emptySet(),
    val skills: Set<String> = emptySet(), // At least 1 common
    val pastInternships: Set<String> = emptySet() // At least 1 common
) {
    fun isEmpty(): Boolean {
        return industries.isEmpty() && skills.isEmpty() && pastInternships.isEmpty()
    }
}

data class SocialFilters(
    val hobbies: Set<String> = emptySet(), // At least 1 common
    val interests: Set<String> = emptySet() // At least 1 common
) {
    fun isEmpty(): Boolean {
        return hobbies.isEmpty() && interests.isEmpty()
    }
}

/**
 * Combined filter state
 */
data class ExploreFilters(
    val academic: AcademicFilters = AcademicFilters(),
    val career: CareerFilters = CareerFilters(),
    val social: SocialFilters = SocialFilters()
) {
    fun getFiltersForSection(section: Sections?): Any {
        return when (section) {
            Sections.ACADEMICS -> academic
            Sections.COOP -> career
            Sections.SOCIAL -> social
            null -> social // Default to social
        }
    }
    
    fun isEmpty(section: Sections?): Boolean {
        return when (section) {
            Sections.ACADEMICS -> academic.isEmpty()
            Sections.COOP -> career.isEmpty()
            Sections.SOCIAL -> social.isEmpty()
            null -> social.isEmpty()
        }
    }
}

