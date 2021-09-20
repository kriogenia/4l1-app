package dev.sotoestevez.allforone.repositories

import dev.sotoestevez.allforone.api.ApiFactory
import dev.sotoestevez.allforone.api.ApiRequest
import dev.sotoestevez.allforone.api.schemas.RefreshRequest
import dev.sotoestevez.allforone.api.schemas.SignInResponse
import dev.sotoestevez.allforone.api.services.AuthService
import dev.sotoestevez.allforone.vo.Session
import dev.sotoestevez.allforone.util.extensions.logDebug

/** Repository to make all the session related operations */
class SessionRepository(
    private val authService: AuthService = ApiFactory.getAuthService()
) {

    /**
     * Tries to sign in the user into the application make a request to the server.
     *
     * @param googleIdToken Google Id Token to use in the authentication
     * @return User credentials returned from the server
     */
    suspend fun signIn(googleIdToken: String): SignInResponse {
        logDebug("Requesting session with GoogleIdToken: $googleIdToken")
       return ApiRequest(suspend { authService.signIn(googleIdToken) }).performRequest()
    }

    /**
     * Sends the current session data to the server to renew the session info
     * and refresh the tokens, retrieving a new session
     *
     * @param session   Current session data
     * @return          New session tokens and expiration timestamp
     */
    suspend fun refreshSession(session: Session): Session {
        logDebug("Refreshing session")
        return ApiRequest(suspend { authService.refresh(RefreshRequest(session.auth, session.refresh)) })
            .performRequest().session
    }

}