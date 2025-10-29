//package com.app.co_opilot.service
//
//import com.app.co_opilot.data.repository.ActivityRepository
//import com.app.co_opilot.domain.Activity
//import com.app.co_opilot.domain.enums.Sections
//import kotlinx.coroutines.test.runTest
//import org.junit.Assert.*
//import org.junit.Before
//import org.junit.Test
//import org.mockito.kotlin.*
//import java.util.*
//
//class ActivityServiceTest {
//
//    private lateinit var activityRepository: ActivityRepository
//    private lateinit var activityService: ActivityService
//
//    private val testActivity = Activity(
//        id = "act1",
//        ownerId = "user1",
//        title = "Study Session",
//        description = "Group study for CS 360",
//        sections = Sections.ACADEMICS,
//        createdAt = Date(),
//        updatedAt = Date(),
//        startAt = Date(),
//        endAt = Date(),
//        tags = listOf("#cs360", "#study")
//    )
//
//    @Before
//    fun setup() {
//        activityRepository = mock()
//        activityService = ActivityService(activityRepository)
//    }
//
//    @Test
//    fun getActivity_returnsActivity_whenFound() = runTest {
//        whenever(activityRepository.getActivity("act1")).thenReturn(testActivity)
//
//        val result = activityService.getActivity("act1")
//
//        assertNotNull(result)
//        assertEquals("Study Session", result?.title)
//        verify(activityRepository).getActivity("act1")
//    }
//
//    @Test
//    fun getActivity_returnsNull_whenNotFound() = runTest {
//        whenever(activityRepository.getActivity("missing")).thenReturn(null)
//
//        val result = activityService.getActivity("missing")
//
//        assertNull(result)
//        verify(activityRepository).getActivity("missing")
//    }
//
//    @Test
//    fun createActivity_delegatesToRepository_whenCalled() = runTest {
//        whenever(
//            activityRepository.createActivity(
//                any(),
//                any(),
//                any(),
//                any(),
//                any(),
//                any(),
//                any(),
//                any(),
//                any(),
//                any()
//            )
//        ).thenReturn(Unit)
//
//        activityService.createActivity(
//            testActivity.id,
//            testActivity.ownerId,
//            testActivity.title,
//            testActivity.description,
//            testActivity.sections,
//            testActivity.createdAt,
//            testActivity.updatedAt,
//            testActivity.startAt,
//            testActivity.endAt,
//            testActivity.tags
//        )
//
//        verify(activityRepository).createActivity(
//            eq(testActivity.id),
//            eq(testActivity.ownerId),
//            eq(testActivity.title),
//            eq(testActivity.description),
//            eq(testActivity.sections),
//            any(),
//            any(),
//            any(),
//            any(),
//            eq(testActivity.tags)
//        )
//    }
//
//    @Test
//    fun updateActivity_returnsTrue_whenUpdateSucceeds() = runTest {
//        whenever(
//            activityRepository.updateActivity(
//                eq("act1"),
//                anyOrNull(),
//                anyOrNull(),
//                anyOrNull(),
//                anyOrNull(),
//                anyOrNull(),
//                anyOrNull(),
//                anyOrNull()
//            )
//        ).thenReturn(true)
//
//        val result = activityService.updateActivity("act1")
//
//        assertTrue(result)
//        verify(activityRepository).updateActivity(
//            eq("act1"),
//            anyOrNull(),
//            anyOrNull(),
//            anyOrNull(),
//            anyOrNull(),
//            anyOrNull(),
//            anyOrNull(),
//            anyOrNull()
//        )
//    }
//
//    @Test
//    fun updateActivity_returnsFalse_whenNotFound() = runTest {
//        whenever(
//            activityRepository.updateActivity(
//                eq("missing"),
//                anyOrNull(),
//                anyOrNull(),
//                anyOrNull(),
//                anyOrNull(),
//                anyOrNull(),
//                anyOrNull(),
//                anyOrNull()
//            )
//        ).thenReturn(false)
//
//        val result = activityService.updateActivity("missing")
//
//        assertFalse(result)
//        verify(activityRepository).updateActivity(
//            eq("missing"),
//            anyOrNull(),
//            anyOrNull(),
//            anyOrNull(),
//            anyOrNull(),
//            anyOrNull(),
//            anyOrNull(),
//            anyOrNull()
//        )
//    }
//
//    @Test
//    fun deleteActivity_returnsTrue_whenSuccessful() = runTest {
//        whenever(activityRepository.deleteActivity("act1")).thenReturn(true)
//
//        val result = activityService.deleteActivity("act1")
//
//        assertTrue(result)
//        verify(activityRepository).deleteActivity("act1")
//    }
//
//    @Test
//    fun deleteActivity_returnsFalse_whenActivityNotFound() = runTest {
//        whenever(activityRepository.deleteActivity("actX")).thenReturn(false)
//
//        val result = activityService.deleteActivity("actX")
//
//        assertFalse(result)
//        verify(activityRepository).deleteActivity("actX")
//    }
//
//    @Test
//    fun getAllActivity_returnsList_whenActivitiesExist() = runTest {
//        whenever(activityRepository.getAllActivity()).thenReturn(listOf(testActivity))
//
//        val result = activityService.getAllActivity()
//
//        assertEquals(1, result.size)
//        assertEquals("act1", result.first().id)
//        verify(activityRepository).getAllActivity()
//    }
//
//    @Test
//    fun getAllActivity_returnsEmptyList_whenNoneExist() = runTest {
//        whenever(activityRepository.getAllActivity()).thenReturn(emptyList())
//
//        val result = activityService.getAllActivity()
//
//        assertTrue(result.isEmpty())
//        verify(activityRepository).getAllActivity()
//    }
//}
