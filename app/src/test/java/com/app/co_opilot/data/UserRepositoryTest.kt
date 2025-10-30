//import com.app.co_opilot.data.SupabaseClient
//import com.app.co_opilot.data.provider.SupabaseProvider
//import com.app.co_opilot.data.repository.UserRepository
//import com.app.co_opilot.domain.User
//import com.app.co_opilot.domain.profile.AcademicProfile
//import com.app.co_opilot.domain.profile.BasicProfile
//import com.app.co_opilot.domain.profile.CareerProfile
//import com.app.co_opilot.domain.profile.SocialProfile
//import io.github.jan.supabase.postgrest.Postgrest
//import io.github.jan.supabase.postgrest.postgrest
//import io.github.jan.supabase.postgrest.query.PostgrestQueryBuilder
//import org.junit.Before
//import org.mockito.Mock
//import org.mockito.MockitoAnnotations
//import org.mockito.kotlin.whenever
//
//@OptIn(ExperimentalCoroutinesApi::class)
//class UserRepositoryTest {
//
//    private lateinit var repository: UserRepository
//
//    @Mock lateinit var provider: SupabaseProvider
//    @Mock lateinit var client: SupabaseClient
//    @Mock lateinit var postgrest: Postgrest
//    @Mock lateinit var table: PostgrestQueryBuilder
//
//    private val testUser = User(
//        id = "123",
//        name = "Benny",
//        email = "test@example.com",
//        avatarUrl = "url",
//        createdAt = "2024-01-01",
//        updatedAt = "2024-01-01",
//        basicProfile = BasicProfile(),
//        academicProfile = AcademicProfile(),
//        careerProfile = CareerProfile(),
//        socialProfile = SocialProfile()
//    )
//
//    @Before
//    fun setup() {
//        MockitoAnnotations.openMocks(this)
//
//        whenever(provider.client).thenReturn(client)
//        whenever(client.postgrest).thenReturn(postgrest)
//        whenever(postgrest["users"]).thenReturn(table)
//
//        repository = UserRepository(provider)
//    }
