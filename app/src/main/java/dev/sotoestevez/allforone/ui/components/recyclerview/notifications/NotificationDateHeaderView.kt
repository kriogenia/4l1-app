package dev.sotoestevez.allforone.ui.components.recyclerview.notifications

import dev.sotoestevez.allforone.R

/**
 * View for date headers on the Notifications dialog
 *
 * @property text   text to display
 */
class NotificationDateHeaderView(val text: String) : NotificationsView {

    override val id: String = text

    override val layoutId: Int = R.layout.content_notifications_date_header

    override val viewType: Int = NotificationsView.Type.DATE.ordinal

}