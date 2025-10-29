//package com.app.co_opilot.domain
//
//import com.app.co_opilot.domain.profile.AcademicProfile
//import com.app.co_opilot.domain.profile.BasicProfile
//import com.app.co_opilot.domain.profile.CareerProfile
//import com.app.co_opilot.domain.profile.SocialProfile
//import org.junit.Test
//import java.util.Date
//import org.junit.Assert.assertNull
//import org.junit.Assert.assertEquals
//import org.junit.Assert.assertNotEquals
//
//class UserTest {
//    @Test
//    fun createUserTest() {
//        val curDate = Date()
//        val user = User(
//            "id1", "name", "t@uwaterloo.ca", null, curDate, curDate, BasicProfile(), AcademicProfile(), CareerProfile(), SocialProfile()
//        )
//        assertNull(user.avatarUrl)
//        assert(user.email.endsWith("@uwaterloo.ca"))
//        assertEquals("id1", user.id)
//        assertEquals("name", user.name)
//        assertEquals(user.createdAt, user.updatedAt)
//        assertEquals(curDate, user.createdAt)
//    }
//
//    @Test
//    fun copyUserEqualityTest() {
//        val user = User(
//            "id1", "name", "t@uwaterloo.ca", null, Date(), Date(), BasicProfile(), AcademicProfile(), CareerProfile(), SocialProfile()
//        )
//        val user2 = user.copy()
//        assertEquals(user, user2)
//    }
//
//    @Test
//    fun copyUserEditInequalityTest() {
//        val user = User(
//            "id1", "name", "t@uwaterloo.ca", null, Date(), Date(), BasicProfile(), AcademicProfile(), CareerProfile(), SocialProfile()
//        )
//        val user2 = user.copy(name = "name2")
//        assertNotEquals(user, user2)
//    }
//}