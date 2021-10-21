package dev.sotoestevez.allforone.api.schemas

import dev.sotoestevez.allforone.vo.Notification
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.vo.feed.Message

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

data class TaskListResponse (
    val tasks: Array<TaskResponse>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TaskListResponse

        if (!tasks.contentEquals(other.tasks)) return false

        return true
    }

    override fun hashCode(): Int {
        return tasks.contentHashCode()
    }
}

/**
 * Model of the response to be received from the /feed/notifications endpoint
 *
 * @property notifications	list of notifications retrieved
 */
data class FeedNotificationsResponse(
    val notifications: Array<Notification>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FeedMessageResponse

        if (!notifications.contentEquals(other.messages)) return false

        return true
    }

    override fun hashCode(): Int {
        return notifications.contentHashCode()
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
 * Model with the property joining of the messages to be received from the /feed/messages endpoint
 *
 * @property submitter      ID of the message submitter
 * @property username	    Username of the message submitter
 * @property timestamp	    Creation timestamp of the message
 * @property lastUpdate	    Last update timestamp of the message
 * @property type		    Type of message
 * @property _id            ID of the message
 * @property message	    Content of the TextMessages
 * @property title	        Title of the TaskMessages
 * @property description	Description of the TaskMessages
 * @property done	        State of the TaskMessages
 */
data class PlainMessage(
    val submitter: String,
    val username: String,
    val timestamp: Long,
    val lastUpdate: Long,
    val type: Message.Type,
    val _id: String? = null,
    val message: String? = null,
    val title: String? = null,
    val description: String? = null,
    val done: Boolean? = null
)