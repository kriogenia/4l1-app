package dev.sotoestevez.allforone.vo

import com.google.gson.annotations.SerializedName
import java.time.Instant

/**
 * Represents a message that can be sent through the Feed
 *
 * @property id unique identifier of the message
 * @property message content of the message
 * @property user author of the message
 * @property timestamp timestamp of the message creation
 */
data class Message(
	@SerializedName("_id") val id: Long = UNSET,
	val message: String,
	val user: User,
	val timestamp: Long = Instant.now().toEpochMilli()
) {

	companion object {
		/** Default id for newly generated messages without id */
		const val UNSET = -1L
	}

}
