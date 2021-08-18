package dev.sotoestevez.allforone.api.responses

import dev.sotoestevez.allforone.data.Session
import dev.sotoestevez.allforone.data.User

/**
 * Model of the response to be received from the /auth/signin endpoint
 *
 * @property session    Session info
 * @property user       Data of the user
 */
data class SignInResponse(
	val session: Session,
	val user: User
)

/**
 * Model of the response to be received from the /auth/refresh endpoint
 *
 * @property session    Session info
 */
data class RefreshResponse(
	val session: Session
)