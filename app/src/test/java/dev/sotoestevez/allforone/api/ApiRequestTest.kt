package dev.sotoestevez.allforone.api

import com.haroldadmin.cnradapter.NetworkResponse
import dev.sotoestevez.allforone.api.responses.BaseErrorResponse
import dev.sotoestevez.allforone.api.responses.SignInResponse
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.util.rules.CoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class ApiRequestTest {

    @get:Rule
    var coroutineRule: CoroutineRule = CoroutineRule()

    private val message = "error"

    @Test
    fun `should return the body of successful requests`(): Unit = coroutineRule.testDispatcher.runBlockingTest {
        val response = SignInResponse("auth", "refresh", 0,
            User("id", "googleId", User.Role.BLANK, "name")
        )
        val networkResponse = NetworkResponse.Success(response, null, 200)
        val req = ApiRequest(suspend { networkResponse }).performRequest()
        assertEquals(response, req)
    }

    @Test
    fun `should return an ErrorResponse with the body of the error with unsuccessful but completed requests`(): Unit =
        coroutineRule.testDispatcher.runBlockingTest {

        val response = BaseErrorResponse(message)
        val networkResponse = NetworkResponse.ServerError(response, 400)
        try {
            @Suppress("IMPLICIT_NOTHING_TYPE_ARGUMENT_IN_RETURN_POSITION")
            ApiRequest(suspend { networkResponse }).performRequest()
        } catch (error: APIErrorException) {
            assertEquals(message, error.message)
        }
    }

    @Test
    fun `should throw an IOException when network error occurs`(): Unit = coroutineRule.testDispatcher.runBlockingTest {
        val networkError = IOException(message)
        val networkResponse = NetworkResponse.NetworkError(networkError)
        try {
            @Suppress("IMPLICIT_NOTHING_TYPE_ARGUMENT_IN_RETURN_POSITION")
            ApiRequest(suspend { networkResponse }).performRequest()
        } catch (error: IOException) {
            assertEquals(networkError, error)
        }
    }

    @Test
    fun `should return a RuntimeException when the request provokes an unknown error`(): Unit =
        coroutineRule.testDispatcher.runBlockingTest {

        val unknownError = RuntimeException(message)
        val networkResponse = NetworkResponse.UnknownError(unknownError)
        try {
            @Suppress("IMPLICIT_NOTHING_TYPE_ARGUMENT_IN_RETURN_POSITION")
            ApiRequest(suspend { networkResponse }).performRequest()
        } catch (error: Throwable) {
            assertEquals(unknownError, error)
        }
    }

}