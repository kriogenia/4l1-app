package dev.sotoestevez.allforone.api.services

import com.haroldadmin.cnradapter.NetworkResponse
import dev.sotoestevez.allforone.api.requests.BondEstablishRequest
import dev.sotoestevez.allforone.api.responses.*
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.util.rules.CoroutineRule
import dev.sotoestevez.allforone.util.rules.WebServerRule
import dev.sotoestevez.allforone.util.webserver.UserDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UserServiceTest {

	@get:Rule
	var coroutineRule: CoroutineRule = CoroutineRule()

	@get:Rule
	var webServerRule: WebServerRule = WebServerRule()

	// Object to test
	private val api: UserService = webServerRule.factory.create(UserService::class.java)

	@Before
	fun beforeEach() {
		webServerRule.mock.dispatcher = UserDispatcher
	}

	@Test
	fun `should manage the bond establishment with a valid request`(): Unit = runBlocking {
		val expected = MessageResponse("The bond has been established")
		val request = BondEstablishRequest("valid")

		val actual = api.bondEstablish("valid", request)

		assertTrue(actual is NetworkResponse.Success)
		assertEquals(expected, (actual as NetworkResponse.Success).body)
	}

	@Test
	fun `should manage the ErrorResponse received from a wrong bond establishment`(): Unit = runBlocking {
		val expected = BaseErrorResponse("The provided token is invalid")
		val request = BondEstablishRequest("invalid")

		val actual = api.bondEstablish("invalid", request)

		assertTrue(actual is NetworkResponse.ServerError)
		assertEquals(expected, (actual as NetworkResponse.ServerError).body)
	}

	@Test
	fun `should retrieve a bonding token for the user`(): Unit = runBlocking {
		val expected = BondGenerateResponse("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")

		val actual = api.bondGenerate("valid")

		assertTrue(actual is NetworkResponse.Success)
		assertEquals(expected, (actual as NetworkResponse.Success).body)
	}

	@Test
	fun `should retrieve the list of bonded users`(): Unit = runBlocking {
		val expected = BondListResponse(arrayOf(
			User(null, null, User.Role.BLANK, "First", "123456789"),
			User(null, null, User.Role.KEEPER, "Second", "987654321"),
			))

		val actual = api.bondList("valid")

		assertTrue(actual is NetworkResponse.Success)
		assertEquals(expected, (actual as NetworkResponse.Success).body)
	}

	@Test
	fun `should retrieve the cared user when it exists`(): Unit = runBlocking {
		val expected = CaredResponse(User(
			"61198ff240cec3067a66c0b1",
			"valid",
			User.Role.PATIENT))

		val actual = api.cared("defined")

		assertTrue(actual is NetworkResponse.Success)
		assertEquals(expected, (actual as NetworkResponse.Success).body)
	}

	@Test
	fun `should retrieve a null cared user when it does not exist`(): Unit = runBlocking {
		val expected = CaredResponse(null)

		val actual = api.cared("undefined")

		assertTrue(actual is NetworkResponse.Success)
		assertEquals(expected, (actual as NetworkResponse.Success).body)
	}

	@Test
	fun `should manage the update with a valid request`(): Unit = runBlocking {
		val expected = MessageResponse("The specified user has been updated successfully")

		val actual = api.update("valid", User("id", "googleId", User.Role.PATIENT))

		assertTrue(actual is NetworkResponse.Success)
		assertEquals(expected, (actual as NetworkResponse.Success).body)
	}

	@Test
	fun `should parse ErrorResponse when requested Update with invalid authorization`(): Unit = runBlocking {
		val expected = BaseErrorResponse("The provided token is invalid")

		val actual = api.update("invalid", User("id", "googleId", User.Role.PATIENT))

		assertTrue(actual is NetworkResponse.ServerError)
		assertEquals(expected, (actual as NetworkResponse.ServerError).body)
	}

}