package dev.sotoestevez.allforone.ui.components.recyclerview.notifications.listeners

import dev.sotoestevez.allforone.vo.Notification

/** Interface to specify NotificationView buttons logic */
interface NotificationListener {

    /** Action to perform when pressing the Go button */
    fun onGo(notification: Notification)

    /** Action to perform when pressing the Read button */
    fun onRead(notification: Notification)

}