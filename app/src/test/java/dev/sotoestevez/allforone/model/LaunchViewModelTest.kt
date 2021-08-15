package dev.sotoestevez.allforone.model

import android.content.SharedPreferences
import dev.sotoestevez.allforone.api.APIErrorException
import dev.sotoestevez.allforone.api.data.SignInResponse
import dev.sotoestevez.allforone.entities.SessionManager
import dev.sotoestevez.allforone.entities.User
import dev.sotoestevez.allforone.repositories.UserRepository
import dev.sotoestevez.allforone.ui.blank.SetUpActivity
import dev.sotoestevez.allforone.util.MainCoroutineRule
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LaunchViewModelTest {

    @get:Rule
    var mainCoroutineRule: MainCoroutineRule = MainCoroutineRule()

    // Objects
    private val user: User = User("id", "valid", User.Role.BLANK)
    private val errorResponse: APIErrorException = APIErrorException("error")

    // Test object
    private lateinit var model: LaunchViewModel

    @Before
    fun beforeEach() {
        mockkObject(UserRepository)
        mockkConstructor(SessionManager::class)
        every { anyConstructed<SessionManager>().openSession(any(), any(), any()) } returns Unit
        model = LaunchViewModel(mockk())
    }

    @After
    fun afterEach() {
        unmockkAll()            // reset all the mocks
    }


    @Test
    fun handleSignInResultWithValidToken(): Unit = runBlockingTest {
        val signInResponse = SignInResponse("auth", "refresh", 0, user)
        coEvery { UserRepository.signIn("valid") } returns signInResponse
        // Perform the authentication
        model.handleSignInResult("valid")
        assertEquals(user, model.user)
        // Check the session was stored
        verify(exactly = 1) { anyConstructed<SessionManager>().openSession(any(), any(), any()) }
        // Check the correct destiny is selected
        assertEquals(SetUpActivity::class.java, model.destiny.value)
    }

}