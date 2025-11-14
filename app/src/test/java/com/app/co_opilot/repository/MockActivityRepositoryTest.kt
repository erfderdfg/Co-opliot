package com.app.co_opilot.repository


import com.app.co_opilot.data.repository.mock.MockActivityRepository
import com.app.co_opilot.domain.Activity
import com.app.co_opilot.domain.enums.Sections
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*
import java.util.*

class MockActivityRepositoryTest {

    private fun act(id: String = "a1") = Activity(
        id = id,
        ownerId = "owner",
        title = "Title",
        description = "Desc",
        sections = Sections.SOCIAL,
        startAt = Date().toInstant().toString(),
        endAt = Date().toInstant().toString(),
        tags = listOf("tag1"),
        createdAt = Date().toInstant().toString(),
        updatedAt = Date().toInstant().toString()
    )

    @Test
    fun `createActivity then getActivity works`() = runTest {
        val repo = MockActivityRepository()
        val now = Date()

        val ok = repo.createActivity(
            id = "a1",
            ownerId = "u1",
            title = "Study Group",
            description = "CS",
            sections = Sections.ACADEMICS,
            createdAt = now,
            updatedAt = now,
            startAt = now,
            endAt = now,
            tags = listOf("cs", "math")
        )
        assertTrue(ok)

        val res = repo.getActivity("a1")
        assertNotNull(res)
        assertEquals("Study Group", res!!.title)
    }

    @Test
    fun `updateActivity changes fields`() = runTest {
        val repo = MockActivityRepository()
        repo.seed(listOf(act("a1")))

        val ok = repo.updateActivity(
            id = "a1",
            title = "New Title",
            tags = listOf("x")
        )
        assertTrue(ok)

        val res = repo.getActivity("a1")!!
        assertEquals("New Title", res.title)
        assertEquals(listOf("x"), res.tags)
    }

    @Test
    fun `deleteActivity removes activity`() = runTest {
        val repo = MockActivityRepository()
        repo.seed(listOf(act("a1")))

        assertTrue(repo.deleteActivity("a1"))
        assertNull(repo.getActivity("a1"))
    }
}
