package dev.sotoestevez.allforone.api.responses

import dev.sotoestevez.allforone.data.Session
import dev.sotoestevez.allforone.data.User

/**
 * Model of the response to be received from the /auth/signin endpoint
 *
 * @property auth       Authentication token
 * @property refresh    Refresh token
 * @property expiration Expiration time of the authentication token
 * @property user       Data of the user
 */
data class SignInResponse(
	val auth: String,
	val refresh: String,
	val expiration: Int,
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