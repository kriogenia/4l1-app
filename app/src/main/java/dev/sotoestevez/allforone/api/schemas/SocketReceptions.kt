package dev.sotoestevez.allforone.api.schemas

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