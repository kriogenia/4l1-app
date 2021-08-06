package dev.sotoestevez.allforone.api.data

/**
 * Model with the used properties from the Google SignInCredentials
 * @property givenName  of the user
 * @property familyName of the user
 * @property id GoogleIdToken of the account
 */
data class GoogleCredentials(
	val id: String?,
	val givenName: String?,
	val familyName: String?,
)

data class Credentials(val auth: String)
