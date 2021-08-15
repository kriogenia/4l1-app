package dev.sotoestevez.allforone.model

import android.content.SharedPreferences
import androidx.lifecycle.viewModelScope
import dev.sotoestevez.allforone.api.APIErrorException
import dev.sotoestevez.allforone.api.data.SignInResponse
import dev.sotoestevez.allforone.entities.User
import dev.sotoestevez.allforone.repositories.UserRepository
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LaunchViewModelTest {

    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("Test")

    @Before @ExperimentalCoroutinesApi @ObsoleteCoroutinesApi
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After @ExperimentalCoroutinesApi @ObsoleteCoroutinesApi
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    // Objects
    private val user: User = User("id", "valid", User.Role.BLANK)
    private val signInResponse: SignInResponse = SignInResponse("auth", "refresh", 0, user)
    private val errorResponse: APIErrorException = APIErrorException("error")

    // Mocks
    private val mockSharedPreferences: SharedPreferences = mockk()

    // Test object
    private val model: LaunchViewModel = LaunchViewModel(mockSharedPreferences)

    @Test @ExperimentalCoroutinesApi
    fun handleSignInResultWithValidToken(): Unit = runBlocking {
        mockkObject(UserRepository)
        coEvery { UserRepository.signIn("valid") } returns signInResponse
        // Perform the authentication
        model.handleSignInResult("valid")
        // Check the user is saved
        assertEquals(user, model.user)
    }

}