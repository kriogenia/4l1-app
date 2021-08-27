package dev.sotoestevez.allforone.api.services

import com.haroldadmin.cnradapter.NetworkResponse
import dev.sotoestevez.allforone.api.responses.BaseErrorResponse
import dev.sotoestevez.allforone.api.responses.MessageResponse
import dev.sotoestevez.allforone.api.responses.SignInResponse
import dev.sotoestevez.allforone.data.Session
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.util.rules.CoroutineRule
import dev.sotoestevez.allforone.util.rules.WebServerRule
import dev.sotoestevez.allforone.util.webserver.UserDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
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
		// Set /auth endpoint mock dispatcher
		webServerRule.mock.dispatcher = UserDispatcher
	}

	@Test
	fun `should manage a good response with a valid request`(): Unit = runBlocking {
		val expected = MessageResponse("The specified user has been updated successfully")

		val actual = api.update("valid", User("id", "googleId", User.Role.PATIENT))
		Assert.assertTrue(actual is NetworkResponse.Success)
		Assert.assertEquals(expected, (actual as NetworkResponse.Success).body)
	}

	@Test
	fun `should parse ErrorResponse when requested with invalid authorization`(): Unit = runBlocking {
		val expected = BaseErrorResponse("The provided token is invalid, please log in again")

		val actual = api.update("invalid", User("id", "googleId", User.Role.PATIENT))
		Assert.assertTrue(actual is NetworkResponse.ServerError)
		Assert.assertEquals(expected, (actual as NetworkResponse.ServerError).body)
	}

}