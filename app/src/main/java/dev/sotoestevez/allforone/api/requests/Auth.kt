package dev.sotoestevez.allforone.api.requests

/**
 * Model of the request body for the auth/refresh endpoint
 *
 * @property auth       Old authentication token to renew
 * @property refresh    Current refresh token
 */
data class RefreshRequest(
	val auth: String,
	val refresh: String
)
