package dev.sotoestevez.allforone.ui.components.exchange.notification

import android.content.Context
import dev.sotoestevez.allforone.R

/**
 * Base class for feed notifications
 *
 * @property message    Code of the message to print
 * @property extraArg   Extra argument to use in the message if any
 */
abstract class FeedNotification(private val message: Int, private val extraArg: String) : TextNotification {

    override fun getString(context: Context) = String.format(context.getString(message), extraArg)
}

/** [TextNotification] for new users joining the room */
class UserJoiningNotification(extraArg: String) : FeedNotification(R.string.user_joined_room, extraArg)

/** [TextNotification] for users leaving the room */
class UserLeavingNotification(extraArg: String) : FeedNotification(R.string.user_left_room, extraArg)

/** [TextNotification] for new messages incoming */
class NewMessageNotification(extraArg: String) : FeedNotification(R.string.new_message, extraArg)