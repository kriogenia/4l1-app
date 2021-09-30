package dev.sotoestevez.allforone.api.schemas

import dev.sotoestevez.allforone.vo.Message
import dev.sotoestevez.allforone.vo.User

/**
 * Model of the response to be received from the /user/bond/generate endpoint
 *
 * @property bonds    List of bonded users
 */
data class BondListResponse(
    val bonds: Array<User>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BondListResponse

        if (!bonds.contentEquals(other.bonds)) return false

        return true
    }

    override fun hashCode(): Int {
        return bonds.contentHashCode()
    }
}

/**
 * Model of the response to be received from the /feed/messages endpoint
 *
 * @property messages	list of messages retrieved
 */
data class FeedMessageResponse(
    val messages: Array<PlainMessage>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FeedMessageResponse

        if (!messages.contentEquals(other.messages)) return false

        return true
    }

    override fun hashCode(): Int {
        return messages.contentHashCode()
    }
}

/**
 * Model of the messages to be received from the /feed/messages endpoint
 *
 * @property _id        ID of the message
 * @property message	Content of the message
 * @property submitter  ID of the message submitter
 * @property username	Username of the message submitter
 * @property timestamp	Creation timestamp of the message
 * @property type		Type of message
 */
data class PlainMessage(
    val _id: String,
    val message: String,
    val submitter: String,
    val username: String,
    val timestamp: Long,
    val type: Message.Type
)