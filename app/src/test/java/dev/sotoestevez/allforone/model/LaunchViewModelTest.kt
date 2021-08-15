package dev.sotoestevez.allforone.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.sotoestevez.allforone.api.APIErrorException
import dev.sotoestevez.allforone.api.data.SignInResponse
import dev.sotoestevez.allforone.entities.SessionManager
import dev.sotoestevez.allforone.entities.User
import dev.sotoestevez.allforone.repositories.UserRepository
import dev.sotoestevez.allforone.ui.blank.SetUpActivity
import dev.sotoestevez.allforone.util.rules.CoroutineRule
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LaunchViewModelTest {

    @get:Rule
    var coroutineRule: CoroutineRule = CoroutineRule()

    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    // Objects
    private val user: User = User("id", "valid", User.Role.BLANK, null)
    private val errorResponse: APIErrorException = APIErrorException("error")

    // Test object
    private lateinit var model: LaunchViewModel

    @Before
    fun beforeEach() {
        mockkObject(UserRepository)
        mockkConstructor(SessionManager::class)
        every { anyConstructed<SessionManager>().openSession(any(), any(), any()) } returns Unit
        model = LaunchViewModel(mockk(), coroutineRule.testDispatcherProvider)
    }

    @After
    fun afterEach() {
        unmockkAll()            // reset all the mocks
    }


    @Test
    fun handleSignInResult_validToken(): Unit = coroutineRule.testDispatcher.runBlockingTest {
        val signInResponse = SignInResponse("auth", "refresh", 0, user)
        coEvery { UserRepository.signIn("valid") } returns signInResponse
        // Perform the authentication
        model.handleSignInResult("valid")
        // Check the session was stored
        verify(exactly = 1) { anyConstructed<SessionManager>().openSession(any(), any(), any()) }
        // Check the model data has been updated
        assertEquals(user, model.user)
        assertEquals(SetUpActivity::class.java, model.destiny.value)
    }

}