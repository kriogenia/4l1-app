package dev.sotoestevez.allforone.ui.components.recyclerview.feed

import android.content.Context
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.vo.notification.Notification

/** View for user's action to notify in the feed */
class FeedNotificationView(private val notification: Notification) : FeedView {

    override val id: String = notification.id

    override val layoutId: Int = R.layout.content_feed_notification

    override val viewType: Int = FeedView.Type.ACTION.ordinal

    /**
     * Prints the notification text
     *
     * @param context   Application context
     */
    fun print(context: Context) = notification.print(context)

}