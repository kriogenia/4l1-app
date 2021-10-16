package dev.sotoestevez.allforone.vo.feed

import com.google.gson.annotations.SerializedName
import dev.sotoestevez.allforone.api.schemas.PlainMessage
import dev.sotoestevez.allforone.util.helpers.TimeFormatter
import dev.sotoestevez.allforone.vo.User
import java.lang.IllegalStateException

/** Interface of messages displayable in the feed */
sealed interface Message {

	/** Different types of messages of the application */
	enum class Type {
		/** Task messages */
		@SerializedName("task") TASK,
		/** Basic and plain text messages */
		@SerializedName("text") TEXT
	}

	/** Unique identifier of the message */
	val id: String

	/** Text content of the message to display in the feed */
	val content: String

	/** User that submitted the message */
	val submitter: User

	/** Timestamp of the message creation */
	val timestamp: Long

	/** Formatted local time of the message */
	val time: String
		get() = TimeFormatter.getTime(timestamp)

	/**
	 * Returns the dto of the message to send
	 */
	fun toDto(): PlainMessage

	/** Message dedicated builder */
	class Builder() {

		/** Data to use in the message construction */
		var data: PlainMessage? = null

		/**
		 * Builds the message with the specified data
		 *
		 * @return	Message built
		 * @throws	IllegalStateException if no data has been set
		 */
		fun build(): Message {
			if (data == null) throw IllegalStateException("Can't build a message with no data")
			return when (data!!.type) {
				Type.TEXT -> TextMessage(this)
				Type.TASK -> TaskMessage(this)
			}
		}

	}

}
