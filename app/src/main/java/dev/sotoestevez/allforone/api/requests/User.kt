package dev.sotoestevez.allforone.api.requests

/**
 * Model of the request body for the user/bond/establish endpoint
 *
 * @property code   Bonding token to verify
 */
data class BondEstablishRequest(
	val code: String
)