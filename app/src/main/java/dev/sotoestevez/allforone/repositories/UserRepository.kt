package dev.sotoestevez.allforone.repositories

import dev.sotoestevez.allforone.api.ApiFactory
import dev.sotoestevez.allforone.api.ApiRequest
import dev.sotoestevez.allforone.api.data.SignInResponse

/**
 * Repository to make all the user related operations
 */
object UserRepository {

    /**
     * Tries to sign in the user into the application make a request to the server.
     *
     * @param googleIdToken Google Id Token to use in the authentication
     * @return User credentials returned from the server
     */
    suspend fun signIn(googleIdToken: String): SignInResponse {
        // Retrieve the auth service
        val service = ApiFactory.getAuthService()
        // And perform the request to sign in
        return ApiRequest(suspend { service.signIn(googleIdToken) }).performRequest()
    }

}