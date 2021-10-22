package dev.sotoestevez.allforone.ui.components.recyclerview.notifications

import android.content.Context
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.ui.components.recyclerview.BindedItemView
import dev.sotoestevez.allforone.ui.components.recyclerview.notifications.listeners.NotificationListener
import dev.sotoestevez.allforone.vo.Notification

/** View for user's action to notify in the feed */
class NotificationView(
    private val notification: Notification,
    private val listener: NotificationListener
) : NotificationsView {

    override val id: String = notification.id

    override val layoutId: Int = R.layout.content_notification

    override val viewType: Int = NotificationsView.Type.NOTIFICATION.ordinal

    /** If the notification view has an active action */
    val hasAction: Boolean = notification.action.destiny != null

    /**
     * Prints the notification text
     *
     * @param context   Application context
     */
    fun print(context: Context) = notification.print(context)

    /** On Go button press listener */
    fun onGoListener() = listener.onGo(notification)

    /** On Read icon press listener */
    fun onIconClickListener() = listener.onRead(notification)

}