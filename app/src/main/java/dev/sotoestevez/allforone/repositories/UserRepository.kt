package dev.sotoestevez.allforone.repositories

import com.haroldadmin.cnradapter.NetworkResponse
import dev.sotoestevez.allforone.api.APIErrorException
import dev.sotoestevez.allforone.api.ApiFactory
import dev.sotoestevez.allforone.api.ApiRequest
import dev.sotoestevez.allforone.api.data.ErrorResponse
import dev.sotoestevez.allforone.api.data.SignInResponse
import kotlinx.coroutines.delay
import java.io.IOException

/**
 * Repository to make all the user related operations
 */
object UserRepository {

    /**
     * Tries to sign in the user into the application make a request to the server.
     *
     * @param googleIdToken Google Id Token to use in the authentication
     * @return User credentials returned from the server
     *
     * @throws APIErrorException for request with non 2xx error
     * @throws IOException for network errors
     * @throws Throwable for unknown errors
     */
    @Throws(APIErrorException::class, IOException::class, Throwable::class)
    suspend fun signIn(googleIdToken: String): SignInResponse {
        // Retrieve the auth service
        val service = ApiFactory.getAuthService()
        // And perform the request to sign in
        return ApiRequest(suspend { service.signIn(googleIdToken) }).performRequest()
    }

}