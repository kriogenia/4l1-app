package dev.sotoestevez.allforone.repositories

import dev.sotoestevez.allforone.api.schemas.SignInResponse
import dev.sotoestevez.allforone.vo.Session

/** Repository to make all the session related operations */
interface SessionRepository {

    /**
     * Tries to sign in the user into the application make a request to the server.
     *
     * @param googleIdToken Google Id Token to use in the authentication
     * @return User credentials returned from the server
     */
    suspend fun signIn(googleIdToken: String): SignInResponse

    /**
     * Sends the current session data to the server to renew the session info
     * and refresh the tokens, retrieving a new session
     *
     * @param session   Current session data
     * @return          New session tokens and expiration timestamp
     */
    suspend fun refreshSession(session: Session): Session

}