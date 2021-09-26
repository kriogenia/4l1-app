package dev.sotoestevez.allforone.repositories.impl

import com.haroldadmin.cnradapter.NetworkResponse
import dev.sotoestevez.allforone.api.ApiRequest
import dev.sotoestevez.allforone.api.schemas.*
import dev.sotoestevez.allforone.api.services.UserService
import dev.sotoestevez.allforone.repositories.UserRepository
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.util.rules.CoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkConstructor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UserRepositoryImplTest {

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
		repo = UserRepositoryImpl(mockUserService)
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
		assertEquals(result, bondGenerateResponse.code)
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

	@Test
	fun `should return the list of bonds of the user`(): Unit = coroutineRule.testDispatcher.runBlockingTest {
		val bondListResponse = BondListResponse(arrayOf(
			User(null, null, User.Role.KEEPER, "First"),
			User(null, null, User.Role.KEEPER, "Second")
		))
		val response: NetworkResponse.Success<BondListResponse> = mockk()
		coEvery { response.code } returns 200
		coEvery { response.body } returns bondListResponse
		coEvery { mockUserService.bondList(any()) } returns response

		val result = repo.getBonds("token")

		coVerify(exactly = 1) { mockUserService.bondList("token") }
		assertTrue(result is ArrayList)
		for (i in result.indices) {
			assertEquals(bondListResponse.bonds[i], result[i])
		}
	}

	@Test
	fun `should return the cared patient of the user`(): Unit = coroutineRule.testDispatcher.runBlockingTest {
		val caredResponse = CaredResponse(User("id", "googleId", User.Role.PATIENT))
		val response: NetworkResponse.Success<CaredResponse> = mockk()
		coEvery { response.code } returns 200
		coEvery { response.body } returns caredResponse
		coEvery { mockUserService.cared(any()) } returns response

		val exists = repo.getCared("token")

		assertEquals(caredResponse.cared, exists)

		coEvery { response.body } returns CaredResponse(null)
		coEvery { mockUserService.cared(any()) } returns response

		val undefined = repo.getCared("token")

		assertNull(undefined)

		coVerify(exactly = 2) { mockUserService.cared("token") }
	}

	@Test
	fun `should send the user update to the server`(): Unit = coroutineRule.testDispatcher.runBlockingTest {
		val updateResponse = MessageResponse("success")
		val response: NetworkResponse.Success<MessageResponse> = mockk()
		coEvery { response.code } returns 200
		coEvery { response.body } returns updateResponse
		coEvery { mockUserService.update(any(), any()) } returns response

		val user = User("id", "googleId", User.Role.PATIENT)
		repo.update(user, "token")

		coVerify(exactly = 1) { mockUserService.update("token", user) }
	}

}