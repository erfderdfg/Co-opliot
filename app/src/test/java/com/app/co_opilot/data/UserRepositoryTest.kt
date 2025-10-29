//package com.app.co_opilot.data
//
//import com.app.co_opilot.data.provider.SupabaseProvider
//import com.app.co_opilot.data.repository.UserRepository
//import com.app.co_opilot.domain.User
//import com.app.co_opilot.domain.profile.*
//import io.github.jan.supabase.postgrest.Postgrest
//import io.github.jan.supabase.postgrest.postgrest
//import kotlinx.coroutines.runBlocking
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.mockito.junit.MockitoJUnitRunner
//import org.mockito.kotlin.*
//import java.util.*
//
//@RunWith(MockitoJUnitRunner::class)
//class UserRepositoryTest {
//
//    private val mockSupabaseProvider = mock<SupabaseProvider>()
//    private val mockClient = mock<io.github.jan.supabase.SupabaseClient>()
//    private val mockPostgrest = mock<Postgrest>()
//    private val repo: UserRepository
//
//    init {
//        whenever(mockSupabaseProvider.client).thenReturn(mockClient)
//        whenever(mockClient.postgrest).thenReturn(mockPostgrest)
//        repo = UserRepository(mockSupabaseProvider)
//    }
//
//    // region 🔹 CREATE USER
//    @Test
//    fun `createUser calls postgrest insert once`() = runBlocking {
//        val id = UUID.randomUUID().toString()
//        val user = User(
//            id = id,
//            name = "Test User",
//            email = "test@example.com",
//            avatarUrl = null,
//            createdAt = Date().toString(),
//            updatedAt = Date().toString(),
//            basicProfile = BasicProfile(),
//            academicProfile = AcademicProfile(),
//            careerProfile = CareerProfile(),
//            socialProfile = SocialProfile()
//        )
//
//        // mock insert table call
//        val mockTable = mock<Postgrest.Table>()
//        whenever(mockPostgrest["users"]).thenReturn(mockTable)
//        whenever(mockTable.insert(any<User>())).thenReturn(Unit)
//
//        repo.createUser(
//            id = id,
//            name = user.name,
//            email = user.email,
//            avatarUrl = user.avatarUrl,
//            createdAt = Date(),
//            updatedAt = Date(),
//            basicProfile = user.basicProfile,
//            academicProfile = user.academicProfile,
//            careerProfile = user.careerProfile,
//            socialProfile = user.socialProfile
//        )
//
//        verify(mockPostgrest)["users"]
//        verify(mockTable, times(1)).insert(any<User>())
//    }
//    // endregion
//
//    // region 🔹 READ USER
//    @Test
//    fun `getUser calls postgrest select once`() = runBlocking {
//        val id = UUID.randomUUID().toString()
//        val mockTable = mock<Postgrest.Table>()
//
//        whenever(mockPostgrest["users"]).thenReturn(mockTable)
//        whenever(
//            mockTable.select(any())
//        ).thenReturn(mock()) // stub Postgrest query result
//        // You can stub decodeSingle<User>() if needed
//
//        repo.getUser(id)
//
//        verify(mockPostgrest)["users"]
//        verify(mockTable, times(1)).select(any())
//    }
//    // endregion
//
//    // region 🔹 READ ALL USERS
//    @Test
//    fun `getAllUsers calls postgrest select once`() = runBlocking {
//        val mockTable = mock<Postgrest.Table>()
//        whenever(mockPostgrest["users"]).thenReturn(mockTable)
//        whenever(mockTable.select()).thenReturn(mock())
//
//        repo.getAllUsers()
//
//        verify(mockPostgrest)["users"]
//        verify(mockTable, times(1)).select()
//    }
//    // endregion
//
//    // region 🔹 UPDATE USER
//    @Test
//    fun `updateUser calls postgrest update once`() = runBlocking {
//        val id = UUID.randomUUID().toString()
//        val mockTable = mock<Postgrest.Table>()
//        whenever(mockPostgrest["users"]).thenReturn(mockTable)
//        whenever(mockTable.update(any<User>(), any())).thenReturn(Unit)
//
//        // Mock getUser call internally
//        val mockUser = User(
//            id = id,
//            name = "Old",
//            email = "old@example.com",
//            avatarUrl = null,
//            createdAt = Date().toString(),
//            updatedAt = Date().toString(),
//            basicProfile = BasicProfile(),
//            academicProfile = AcademicProfile(),
//            careerProfile = CareerProfile(),
//            socialProfile = SocialProfile()
//        )
//        val spyRepo = spy(repo)
//        doReturn(mockUser).whenever(spyRepo).getUser(id)
//
//        spyRepo.updateUser(id, name = "Updated User")
//
//        verify(mockPostgrest)["users"]
//        verify(mockTable, times(1)).update(any<User>(), any())
//    }
//    // endregion
//
//    // region 🔹 DELETE USER
//    @Test
//    fun `deleteUser calls postgrest delete once`() = runBlocking {
//        val id = UUID.randomUUID().toString()
//        val mockTable = mock<Postgrest.Table>()
//        whenever(mockPostgrest["users"]).thenReturn(mockTable)
//        whenever(mockTable.delete(any())).thenReturn(Unit)
//
//        repo.deleteUser(id)
//
//        verify(mockPostgrest)["users"]
//        verify(mockTable, times(1)).delete(any())
//    }
//    // endregion
//}
