package dev.sotoestevez.allforone.repositories

import com.haroldadmin.cnradapter.NetworkResponse
import dev.sotoestevez.allforone.api.ApiFactory
import dev.sotoestevez.allforone.api.ApiRequest
import dev.sotoestevez.allforone.api.schemas.RefreshRequest
import dev.sotoestevez.allforone.api.responses.BondGenerateResponse
import dev.sotoestevez.allforone.api.responses.ErrorResponse
import dev.sotoestevez.allforone.api.responses.RefreshResponse
import dev.sotoestevez.allforone.api.responses.SignInResponse
import dev.sotoestevez.allforone.api.services.AuthService
import dev.sotoestevez.allforone.data.Session
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.util.rules.CoroutineRule
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SessionRepositoryTest {

    @get:Rule
    var coroutineRule: CoroutineRule = CoroutineRule()

    // Mock object
    private lateinit var mockAuthService: AuthService

    // Test object
    private lateinit var sessionRepository: SessionRepository

    @Before
    fun beforeEach() {
        mockAuthService = mockk()
        mockkConstructor(ApiRequest::class)
        sessionRepository = SessionRepository(mockAuthService)
    }

    @Test
    fun `should return the sign in response when provided a valid token`(): Unit = coroutineRule.testDispatcher.runBlockingTest {
        val signInResponse = SignInResponse(
            Session("auth", "refresh", 0),
            User("id", "googleId", User.Role.BLANK)
        )
        val response: NetworkResponse.Success<SignInResponse> = mockk()
        coEvery { response.code } returns 200
        coEvery { response.body } returns signInResponse
        coEvery { mockAuthService.signIn(any()) } returns response

        val result = sessionRepository.signIn("valid")

        assertEquals(result, signInResponse)
        coVerify(exactly = 1) { mockAuthService.signIn("valid") }
    }

    @Test
    fun `should return the refresh response when provided a valid token`(): Unit = coroutineRule.testDispatcher.runBlockingTest {
        val refreshResponse = RefreshResponse(
            Session("auth", "refresh", 0)
        )
        val response: NetworkResponse.Success<RefreshResponse> = mockk()
        coEvery { response.code } returns 200
        coEvery { response.body } returns refreshResponse
        coEvery { mockAuthService.refresh(any()) } returns response

        val result = sessionRepository.refreshSession(Session("auth", "refresh", 0))

        assertEquals(result, refreshResponse.session)
        coVerify(exactly = 1) { mockAuthService.refresh(RefreshRequest("auth", "refresh")) }
    }

}