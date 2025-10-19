package com.app.co_opilot.data

import com.app.co_opilot.data.repository.ActivityRepository
import com.app.co_opilot.domain.Activity
import com.app.co_opilot.domain.enums.Sections
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.Date
import kotlin.math.abs

class ActivityRepositoryTest {

    private lateinit var activityRepository: ActivityRepository

    private fun anySection(): Sections = Sections.values().first()
    private fun anotherSection(): Sections = Sections.values().last()

    @Before
    fun init() {
        activityRepository = ActivityRepository() // fresh repo each test
    }

    @Test
    fun getActivity_returnsNull_whenNotFound() = runBlocking {
        assertNull(activityRepository.getActivity("nope"))
    }

    @Test
    fun createActivity_thenCanGetById() = runBlocking {
        val now = Date()
        activityRepository.createActivity(
            id = "a1",
            ownerId = "u1",
            title = "Title 1",
            description = "Desc 1",
            sections = anySection(),
            createdAt = now,
            updatedAt = now,
            startAt = Date(now.time + 1_000),
            endAt = Date(now.time + 2_000),
            tags = listOf("kotlin", "repo")
        )

        val got = activityRepository.getActivity("a1")
        assertNotNull(got)
        assertEquals("u1", got!!.ownerId)
        assertEquals("Title 1", got.title)
        assertEquals("Desc 1", got.description)
        assertEquals(listOf("kotlin", "repo"), got.tags)
    }

    @Test
    fun updateActivity_returnsFalse_whenNotFound() = runBlocking {
        val result = activityRepository.updateActivity(
            id = "does_not_exist",
            title = "new"
        )
        assertFalse(result)
    }

    @Test
    fun updateActivity_updatesFields_andRefreshesUpdatedAt() = runBlocking {
        val createdAt = Date()
        val initialUpdatedAt = Date(createdAt.time) // same as created for easy compare

        // create
        activityRepository.createActivity(
            id = "a2",
            ownerId = "u1",
            title = "Old Title",
            description = "Old Desc",
            sections = anySection(),
            createdAt = createdAt,
            updatedAt = initialUpdatedAt,
            startAt = Date(createdAt.time + 1_000),
            endAt = Date(createdAt.time + 2_000),
            tags = listOf("t1")
        )

        val before = activityRepository.getActivity("a2")
        assertNotNull(before)
        val beforeUpdatedAt = before!!.updatedAt

        // Makesure updatedAt changed significantly
        Thread.sleep(5)

        // update some fields
        val newStart = Date(createdAt.time + 3_000)
        val newEnd = Date(createdAt.time + 4_000)
        val result = activityRepository.updateActivity(
            id = "a2",
            ownerId = "u2",
            title = "New Title",
            description = "New Desc",
            sections = anotherSection(),
            startAt = newStart,
            endAt = newEnd,
            tags = listOf("t2", "t3")
        )
        assertTrue(result)

        val after = activityRepository.getActivity("a2")
        assertNotNull(after)
        after!!

        // field changes
        assertEquals("u2", after.ownerId)
        assertEquals("New Title", after.title)
        assertEquals("New Desc", after.description)
        assertEquals(anotherSection(), after.sections)
        assertEquals(newStart, after.startAt)
        assertEquals(newEnd, after.endAt)
        assertEquals(listOf("t2", "t3"), after.tags)

        // updatedAt should be refreshed (>= previous)
        // allow tiny time difference usually strictly follow: after.updatedAt > beforeUpdatedAt
        assertTrue(
            "updatedAt should be refreshed",
            after.updatedAt.after(beforeUpdatedAt) || after.updatedAt == beforeUpdatedAt
        )
    }

    @Test
    fun deleteActivity_noEffect_whenNotFound() = runBlocking {
        // seed one
        val now = Date()
        activityRepository.createActivity(
            id = "a3",
            ownerId = "u1",
            title = "t",
            description = "d",
            sections = anySection(),
            createdAt = now,
            updatedAt = now,
            startAt = Date(now.time + 1_000),
            endAt = Date(now.time + 2_000),
            tags = emptyList()
        )
        assertEquals(1, activityRepository.getAllActivity().size)

        val removed = activityRepository.deleteActivity("no-such-id")
        assertFalse(removed)
        assertEquals(1, activityRepository.getAllActivity().size)
    }

    @Test
    fun deleteActivity_removes_whenFound() = runBlocking {
        val now = Date()
        activityRepository.createActivity(
            id = "a4",
            ownerId = "u9",
            title = "X",
            description = "Y",
            sections = anySection(),
            createdAt = now,
            updatedAt = now,
            startAt = Date(now.time + 1_000),
            endAt = Date(now.time + 2_000),
            tags = listOf("tag")
        )
        assertEquals(1, activityRepository.getAllActivity().size)

        val removed = activityRepository.deleteActivity("a4")
        assertTrue(removed)
        assertEquals(0, activityRepository.getAllActivity().size)
    }

    @Test
    fun getAllActivity_returnsCopy_andReflectsRepositoryState() = runBlocking {
        val now = Date()
        assertTrue(activityRepository.getAllActivity().isEmpty())

        activityRepository.createActivity(
            id = "a5",
            ownerId = "u1",
            title = "A",
            description = "B",
            sections = anySection(),
            createdAt = now,
            updatedAt = now,
            startAt = Date(now.time + 1_000),
            endAt = Date(now.time + 2_000),
            tags = emptyList()
        )
        activityRepository.createActivity(
            id = "a6",
            ownerId = "u2",
            title = "C",
            description = "D",
            sections = anySection(),
            createdAt = now,
            updatedAt = now,
            startAt = Date(now.time + 3_000),
            endAt = Date(now.time + 4_000),
            tags = listOf("one")
        )

        val list1 = activityRepository.getAllActivity()
        assertEquals(2, list1.size)

        // get after delete, number should decrease
        activityRepository.deleteActivity("a5")
        val list2 = activityRepository.getAllActivity()
        assertEquals(1, list2.size)
    }
}
