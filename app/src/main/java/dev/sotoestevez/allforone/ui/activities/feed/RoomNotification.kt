package dev.sotoestevez.allforone.ui.activities.feed

import dev.sotoestevez.allforone.R

/** Encapsulation of the needed notification */
sealed interface RoomNotification {
    /** Code of the message to print */
    val message: Int
    /** Extra argument to use in the message if any */
    val extraArg: String

}

class NewUserJoiningNotification(override val extraArg: String): RoomNotification {

    override val message: Int = R.string.user_joined_room

}

class NewMessageNotification: RoomNotification {

    override val message: Int = R.string.new_message
    override val extraArg: String = ""

}