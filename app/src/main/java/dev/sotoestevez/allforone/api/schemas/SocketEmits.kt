package dev.sotoestevez.allforone.api.schemas

// Schemas of the different objects to send in socket even emissions

/**
 * Object with the needed information to subscribe to the Global room
 *
 * @property id unique id of the current user
 * @property owner id of the owner of the room, the Patient
 */
data class GlobalSubscribe(
	val id: String,
	val owner: String
)

/**
 * Object with the needed information to notify about the start of the location sharing
 *
 * @property id unique id of the current user
 * @property displayName display name of the current user
 */
data class UserInfo(
	val id: String,
	val displayName: String
)