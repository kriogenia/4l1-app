package dev.sotoestevez.allforone.api.requests

/**
 * Message with the needed information to subscribe to the Global room
 *
 * @property id unique id of the current user
 * @property owner id of the owner of the room, the Patient
 */
data class GlobalSubscribe(
	val id: String,
	val owner: String
)