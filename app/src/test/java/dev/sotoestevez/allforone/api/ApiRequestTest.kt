package dev.sotoestevez.allforone.api

import com.haroldadmin.cnradapter.NetworkResponse
import dev.sotoestevez.allforone.api.data.BaseErrorResponse
import dev.sotoestevez.allforone.api.data.SignInResponse
import dev.sotoestevez.allforone.entities.User
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
    fun performRequest_success(): Unit = coroutineRule.testDispatcher.runBlockingTest {
        val response = SignInResponse("auth", "refresh", 0,
            User("id", "googleId", User.Role.BLANK, "name"))
        val networkResponse = NetworkResponse.Success(response, null, 200)
        val req = ApiRequest(suspend { networkResponse }).performRequest()
        assertEquals(response, req)
    }

    @Test
    fun performRequest_serverError(): Unit = coroutineRule.testDispatcher.runBlockingTest {
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
    fun performRequest_networkError(): Unit = coroutineRule.testDispatcher.runBlockingTest {
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
    fun performRequest_unknownError(): Unit = coroutineRule.testDispatcher.runBlockingTest {
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