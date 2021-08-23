package dev.sotoestevez.allforone.repositories

import dev.sotoestevez.allforone.api.ApiFactory
import dev.sotoestevez.allforone.api.ApiRequest
import dev.sotoestevez.allforone.api.responses.ErrorResponse
import dev.sotoestevez.allforone.api.responses.SignInResponse
import dev.sotoestevez.allforone.data.Session
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.util.rules.CoroutineRule
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UserRepositoryTest {

    @get:Rule
    var coroutineRule: CoroutineRule = CoroutineRule()

    @Before
    fun beforeEach() {
        mockkObject(ApiFactory)
        every { ApiFactory.getAuthService() } returns mockk()
        mockkConstructor(ApiRequest::class)
    }

    @Test
    fun `should return the sign in response when provided a valid token`(): Unit = coroutineRule.testDispatcher.runBlockingTest {
        // Mock APIRequest
        val signInResponse = SignInResponse(
            Session("auth", "refresh", 0),
            User("id", "googleId", User.Role.BLANK, "name")
        )
        coEvery { anyConstructed<ApiRequest<SignInResponse, ErrorResponse>>().performRequest() } returns signInResponse
        // Perform the operation
        val result = UserRepository.signIn("valid")
        // Check the result
        assertEquals(result, signInResponse)
    }

}