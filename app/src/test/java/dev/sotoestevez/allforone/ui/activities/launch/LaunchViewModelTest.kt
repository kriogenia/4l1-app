package dev.sotoestevez.allforone.ui.activities.launch

import android.app.Activity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.sotoestevez.allforone.api.APIErrorException
import dev.sotoestevez.allforone.api.schemas.SignInResponse
import dev.sotoestevez.allforone.vo.Session
import dev.sotoestevez.allforone.util.helpers.SessionManager
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.ui.activities.setup.SetUpActivity
import dev.sotoestevez.allforone.ui.activities.keeper.KeeperMainActivity
import dev.sotoestevez.allforone.ui.activities.patient.PatientMainActivity
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

    // Mocks
    private lateinit var mockSessionRepository: SessionRepository

    // Test object
    private lateinit var model: LaunchViewModel

    @Before
    fun beforeEach() {
        // Mocks
        mockSessionRepository = mockk()
        mockkConstructor(SessionManager::class)
        every { anyConstructed<SessionManager>().setSession(any()) } returns Unit
        // Init test object
        model = LaunchViewModel(mockk(), coroutineRule.testDispatcherProvider, mockSessionRepository)
    }

    @After
    fun afterEach() {
        unmockkAll()            // reset all the mocks
    }

    @Test
    fun `should handle the sign in as expected when given a valid token`(): Unit = coroutineRule.testDispatcher.runBlockingTest {
        val user = User("id", User.Role.BLANK, null)
        val signInResponse = SignInResponse(Session("auth", "refresh", 0), user)
        coEvery { mockSessionRepository.signIn("valid") } returns signInResponse
        // Perform the authentication
        model.handleSignInResult("valid")
        // Check the session was stored
        verify(exactly = 1) { anyConstructed<SessionManager>().setSession(any()) }
        // Check the model data has been updated
        assertEquals(user, model.user.value)
        assertEquals(SetUpActivity::class.java, model.destiny.value)
    }

    @Test
    fun `should throw and manage an error when given an invalid token`(): Unit = coroutineRule.testDispatcher.runBlockingTest {
        val errorResponse = APIErrorException("error")
        coEvery { mockSessionRepository.signIn("invalid") } throws errorResponse
        // Perform the authentication
        model.handleSignInResult("invalid")
        // Check the error update
        assertEquals(errorResponse, model.error.value)
    }

    @Test
    fun `should update the destiny accordingly to the user role`(): Unit = coroutineRule.testDispatcher.runBlockingTest {
        // No Role
        checkDestinyByRole(User.Role.BLANK, SetUpActivity::class.java)
        // Keeper role
        checkDestinyByRole(User.Role.KEEPER, KeeperMainActivity::class.java)
        // Patient role
        checkDestinyByRole(User.Role.PATIENT, PatientMainActivity::class.java)
    }

    private fun checkDestinyByRole(role: User.Role, destiny: Class<out Activity>) {
        val user = User("id", role, null)
        val signInResponse = SignInResponse(Session("auth", "refresh", 0), user)
        coEvery { mockSessionRepository.signIn("valid") } returns signInResponse
        model.handleSignInResult("valid")
        assertEquals(destiny, model.destiny.value)
    }

}