package dev.sotoestevez.allforone.api.schemas

// Schemas of the different body requests to communicate with the API

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

/**
 * Model of the request body for the user/bond/establish endpoint
 *
 * @property code   Bonding token to verify
 */
data class BondEstablishRequest(
	val code: String
)

