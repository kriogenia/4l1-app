package dev.sotoestevez.allforone.repositories

import dev.sotoestevez.allforone.api.ApiFactory
import dev.sotoestevez.allforone.api.ApiRequest
import dev.sotoestevez.allforone.api.requests.RefreshRequest
import dev.sotoestevez.allforone.api.responses.RefreshResponse
import dev.sotoestevez.allforone.api.responses.SignInResponse
import dev.sotoestevez.allforone.data.Session

/** Repository to make all the session related operations */
object SessionRepository {

    /**
     * Tries to sign in the user into the application make a request to the server.
     *
     * @param googleIdToken Google Id Token to use in the authentication
     * @return User credentials returned from the server
     */
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