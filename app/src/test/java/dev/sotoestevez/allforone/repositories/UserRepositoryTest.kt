package dev.sotoestevez.allforone.repositories

import com.haroldadmin.cnradapter.NetworkResponse
import dev.sotoestevez.allforone.api.ApiRequest
import dev.sotoestevez.allforone.api.requests.BondEstablishRequest
import dev.sotoestevez.allforone.api.responses.BaseErrorResponse
import dev.sotoestevez.allforone.api.responses.BondGenerateResponse
import dev.sotoestevez.allforone.api.responses.ErrorResponse
import dev.sotoestevez.allforone.api.responses.MessageResponse
import dev.sotoestevez.allforone.api.services.UserService
import dev.sotoestevez.allforone.util.rules.CoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkConstructor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UserRepositoryTest {

	@get:Rule
	var coroutineRule: CoroutineRule = CoroutineRule()

	// Mocks
	private lateinit var mockUserService: UserService

	// Test object
	private lateinit var repo: UserRepository

	@Before
	fun beforeEach() {
		mockUserService = mockk()
		mockkConstructor(ApiRequest::class)
		repo = UserRepository(mockUserService)
	}

	@Test
	fun `should return a bonding token`(): Unit = coroutineRule.testDispatcher.runBlockingTest {
		val bondGenerateResponse = BondGenerateResponse("bonding")
		val response: NetworkResponse.Success<BondGenerateResponse> = mockk()
		coEvery { response.code } returns 200
		coEvery { response.body } returns bondGenerateResponse
		coEvery { mockUserService.bondGenerate(any()) } returns response

		val result = repo.requestBondingCode("token")

		coVerify(exactly = 1) { mockUserService.bondGenerate("token") }
		Assert.assertEquals(result, bondGenerateResponse.code)
	}

	@Test
	fun `should send the code to the server`(): Unit = coroutineRule.testDispatcher.runBlockingTest {
		val bondEstablishResponse = MessageResponse("success")
		val response: NetworkResponse.Success<MessageResponse> = mockk()
		coEvery { response.code } returns 200
		coEvery { response.body } returns bondEstablishResponse
		coEvery { mockUserService.bondEstablish(any(), any()) } returns response

		repo.sendBondingCode("valid", "token")

		coVerify(exactly = 1) { mockUserService.bondEstablish("token", BondEstablishRequest("valid")) }
	}

}