package dev.sotoestevez.allforone.api.data

import dev.sotoestevez.allforone.entities.User

/**
 * Model of the response to be received from the /auth/signin endpoint
 *
 * @property auth       Auth token
 * @property refresh    Refresh token
 * @property expiration time of the auth token
 * @property user       data of the user
 */
data class SignInResponse(
	val auth: String,
	val refresh: String,
	val expiration: Int,
	val user: User
)