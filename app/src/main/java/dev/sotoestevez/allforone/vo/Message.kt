package dev.sotoestevez.allforone.vo

import com.google.gson.annotations.SerializedName
import java.time.Instant
import java.util.*

/**
 * Represents a message that can be sent through the Feed
 *
 * @property id Unique identifier of the message
 * @property message Content of the message
 * @property user Author of the message
 * @property timestamp Timestamp of the message creation
 * @property type Type of message (task or text)
 */
data class Message(
	@SerializedName("_id") val id: String = "",
	val message: String,
	val user: User,
	val type: Type,
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
