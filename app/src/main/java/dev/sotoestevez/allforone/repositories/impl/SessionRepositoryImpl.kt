package dev.sotoestevez.allforone.repositories.impl

import dev.sotoestevez.allforone.api.ApiRequest
import dev.sotoestevez.allforone.api.schemas.SignInResponse
import dev.sotoestevez.allforone.api.services.AuthService
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.vo.Session
import dev.sotoestevez.allforone.util.extensions.logDebug

/** Repository to make all the session related operations */
class SessionRepositoryImpl(
    private val service: AuthService
) : SessionRepository {

    override suspend fun signIn(googleIdToken: String): SignInResponse {
        logDebug("Requesting session with GoogleIdToken: $googleIdToken")
        return ApiRequest(suspend { service.getSignIn(googleIdToken) }).performRequest()
    }

    override suspend fun signOut(token: String) {
        val authToken = token.split(" ")[1]
        ApiRequest(suspend { service.deleteSession(token, authToken) }).performRequest()
    }

    override suspend fun refreshSession(session: Session): Session {
        logDebug("Refreshing session")
        return ApiRequest(suspend { service.getRefresh("Bearer ${session.auth}", session.refresh) })
            .performRequest().session
    }

}