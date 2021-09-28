package dev.sotoestevez.allforone.ui.activities.feed.communication

import android.content.Context
import dev.sotoestevez.allforone.R

/** Encapsulation of the needed notification */
sealed interface RoomNotification {
    /** Code of the message to print */
    val message: Int
    /** Extra argument to use in the message if any */
    val extraArg: String

    /**
     * Generates the notification full string
     *
     * @param context   Context to load the string resource
     */
    fun getString(context: Context) = String.format(context.getString(message), extraArg)

}

/** [RoomNotification] for new users joining the room */
data class UserJoiningNotification(override val extraArg: String): RoomNotification {
    override val message: Int = R.string.user_joined_room
}

/** [RoomNotification] for users leaving the room */
data class UserLeavingNotification(override val extraArg: String): RoomNotification {
    override val message: Int = R.string.user_left_room
}

/** [RoomNotification] for new messages incoming */
class NewMessageNotification(override val extraArg: String): RoomNotification {
    override val message: Int = R.string.new_message
}