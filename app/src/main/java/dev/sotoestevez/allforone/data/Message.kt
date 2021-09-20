package dev.sotoestevez.allforone.data

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
	@SerializedName("_id") val id: Long,
	val message: String,
	val user: User,
	val timestamp: Long
) {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Message

		if (id != other.id) return false

		return true
	}

	override fun hashCode(): Int {
		return id.hashCode()
	}

}
