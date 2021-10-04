package dev.sotoestevez.allforone.vo.feed

import com.google.gson.annotations.SerializedName
import dev.sotoestevez.allforone.util.helpers.TimeFormatter
import dev.sotoestevez.allforone.vo.User
import java.time.Instant

/**
 * Represents a simple text message that can be sent through the Feed
 *
 * @property message 	Content of the message
 */
data class TextMessage(
	@SerializedName("_id") override val id: String = "",
	val message: String,
	override val submitter: User,
	override val timestamp: Long = Instant.now().toEpochMilli()
) : Message {

	override val content: String
		get() = message

	constructor(builder: Message.Builder): this(
		builder.data!!._id,
		builder.data!!.message!!,
		User(id = builder.data!!.submitter, displayName = builder.data!!.username),
		builder.data!!.timestamp
	)

}
