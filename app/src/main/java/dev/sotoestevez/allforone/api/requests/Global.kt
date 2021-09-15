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


data class LocationShare(
	val id: String,
	val displayName: String
)

/**
 * Message sent from the server when an user joins the Global room
 *
 * @property message message of the subscription
 * @property id id of the user subscribed
 * @property displayName name of the user subscribed
 */
data class RoomSubscription(
	val message: String,
	val id: String,
	val displayName: String
)