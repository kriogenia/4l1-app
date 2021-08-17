package dev.sotoestevez.allforone.model

import android.app.Activity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.sotoestevez.allforone.api.APIErrorException
import dev.sotoestevez.allforone.api.data.SignInResponse
import dev.sotoestevez.allforone.entities.SessionManager
import dev.sotoestevez.allforone.entities.User
import dev.sotoestevez.allforone.repositories.UserRepository
import dev.sotoestevez.allforone.ui.blank.SetUpActivity
import dev.sotoestevez.allforone.ui.keeper.KMainActivity
import dev.sotoestevez.allforone.ui.patient.PMainActivity
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

    // Test object
    private lateinit var model: LaunchViewModel

    @Before
    fun beforeEach() {
        // Mocks
        mockkObject(UserRepository)
        mockkConstructor(SessionManager::class)
        every { anyConstructed<SessionManager>().setSession(any(), any(), any()) } returns Unit
        // Init test object
        model = LaunchViewModel(mockk(), coroutineRule.testDispatcherProvider)
    }

    @After
    fun afterEach() {
        unmockkAll()            // reset all the mocks
    }


    @Test
    fun `should handle the sign in as expected when given a valid token`(): Unit = coroutineRule.testDispatcher.runBlockingTest {
        val user = User("id", "valid", User.Role.BLANK, null)
        val signInResponse = SignInResponse("auth", "refresh", 0, user)
        coEvery { UserRepository.signIn("valid") } returns signInResponse
        // Perform the authentication
        model.handleSignInResult("valid")
        // Check the session was stored
        verify(exactly = 1) { anyConstructed<SessionManager>().setSession(any(), any(), any()) }
        // Check the model data has been updated
        assertEquals(user, model.user)
        assertEquals(SetUpActivity::class.java, model.destiny.value)
    }

    @Test
    fun `should throw and manage an error when given an invalid token`(): Unit = coroutineRule.testDispatcher.runBlockingTest {
        val errorResponse = APIErrorException("error")
        coEvery { UserRepository.signIn("invalid") } throws errorResponse
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
        checkDestinyByRole(User.Role.KEEPER, KMainActivity::class.java)
        // Patient role
        checkDestinyByRole(User.Role.PATIENT, PMainActivity::class.java)
    }

    private fun checkDestinyByRole(role: User.Role, destiny: Class<out Activity>) {
        val user = User("id", "valid", role, null)
        val signInResponse = SignInResponse("auth", "refresh", 0, user)
        coEvery { UserRepository.signIn("valid") } returns signInResponse
        model.handleSignInResult("valid")
        assertEquals(destiny, model.destiny.value)
    }

}