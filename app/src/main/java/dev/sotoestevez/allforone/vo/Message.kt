package dev.sotoestevez.allforone.vo

import com.google.gson.annotations.SerializedName
import dev.sotoestevez.allforone.util.helpers.TimeFormatter
import java.time.Instant

/**
 * Represents a message that can be sent through the Feed
 *
 * @property id 		Unique identifier of the message
 * @property message 	Content of the message
 * @property submitter 	Author of the message
 * @property timestamp 	Timestamp of the message creation
 * @property type 		Type of message (task or text)
 */
data class Message(
	@SerializedName("_id") val id: String = "",
	val message: String,
	val submitter: User,	// TODO convert to TextMessage, create MessageBuilder
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

	/** Formatted local time of the message */
	val time: String
		get() = TimeFormatter.getTime(timestamp)

}
