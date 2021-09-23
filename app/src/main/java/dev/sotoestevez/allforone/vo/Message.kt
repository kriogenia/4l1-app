package dev.sotoestevez.allforone.vo

import com.google.gson.annotations.SerializedName
import java.time.Instant
import java.util.*

/**
 * Represents a message that can be sent through the Feed
 *
 * @property id unique identifier of the message
 * @property message content of the message
 * @property user author of the message
 * @property timestamp timestamp of the message creation
 */
data class Message(
	@SerializedName("_id") val id: String = "",
	val message: String,
	val user: User,
	val timestamp: Long = Instant.now().toEpochMilli()
) {

	/** Different types of messages of the application */
	enum class Type {
		/** Task messages */
		@SerializedName("task") TASK,
		/** Basic and plain text messages */
		@SerializedName("text") TEXT
	}

}
