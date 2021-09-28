package dev.sotoestevez.allforone.repositories.impl

import com.haroldadmin.cnradapter.NetworkResponse
import dev.sotoestevez.allforone.api.ApiRequest
import dev.sotoestevez.allforone.api.schemas.RefreshRequest
import dev.sotoestevez.allforone.api.schemas.RefreshResponse
import dev.sotoestevez.allforone.api.schemas.SignInResponse
import dev.sotoestevez.allforone.api.services.AuthService
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.vo.Session
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.util.rules.CoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkConstructor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SessionRepositoryImplTest {

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
        sessionRepository = SessionRepositoryImpl(mockAuthService)
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
        coEvery { mockAuthService.refresh(any(), any()) } returns response

        val result = sessionRepository.refreshSession(Session("auth", "refresh", 0))

        assertEquals(result, refreshResponse.session)
        coVerify(exactly = 1) { mockAuthService.refresh("Bearer auth", "refresh") }
    }

}