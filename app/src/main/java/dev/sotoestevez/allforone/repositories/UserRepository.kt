package dev.sotoestevez.allforone.repositories

import dev.sotoestevez.allforone.api.APIErrorException
import dev.sotoestevez.allforone.api.ApiFactory
import dev.sotoestevez.allforone.api.ApiRequest
import dev.sotoestevez.allforone.api.requests.RefreshRequest
import dev.sotoestevez.allforone.api.responses.RefreshResponse
import dev.sotoestevez.allforone.api.responses.SignInResponse
import dev.sotoestevez.allforone.data.Session
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
        val service = ApiFactory.getAuthService()
        return ApiRequest(suspend { service.signIn(googleIdToken) }).performRequest()
    }

    /**
     * Sends the current session data to the server to renew the session info
     * and refresh the tokens
     *
     * @param session   Current session data
     * @return          New session tokens and expiration timestamp
     */
    suspend fun refreshSession(session: Session): RefreshResponse {
        val service = ApiFactory.getAuthService()
        return ApiRequest(suspend { service.refresh(RefreshRequest(session.auth, session.refresh)) }).performRequest()
    }

}