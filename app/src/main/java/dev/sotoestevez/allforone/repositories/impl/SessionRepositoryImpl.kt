package dev.sotoestevez.allforone.repositories.impl

import dev.sotoestevez.allforone.api.ApiRequest
import dev.sotoestevez.allforone.api.schemas.RefreshRequest
import dev.sotoestevez.allforone.api.schemas.SignInResponse
import dev.sotoestevez.allforone.api.services.AuthService
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.vo.Session
import dev.sotoestevez.allforone.util.extensions.logDebug

/** Repository to make all the session related operations */
class SessionRepositoryImpl(
    private val authService: AuthService
): SessionRepository {

    override suspend fun signIn(googleIdToken: String): SignInResponse {
        logDebug("Requesting session with GoogleIdToken: $googleIdToken")
       return ApiRequest(suspend { authService.signIn(googleIdToken) }).performRequest()
    }

    override suspend fun refreshSession(session: Session): Session {
        logDebug("Refreshing session")
        return ApiRequest(suspend { authService.refresh(RefreshRequest(session.auth, session.refresh)) })
            .performRequest().session
    }

}