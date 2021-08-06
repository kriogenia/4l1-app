package dev.sotoestevez.allforone.api.data

/**
 * Model with the used properties from the Google SignInCredentials
 * @property id GoogleIdToken of the account
 */
data class GoogleCredentials(
	val id: String?,
)

data class Credentials(val auth: String)
