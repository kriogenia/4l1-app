package dev.sotoestevez.allforone.util.helpers.notifications

import android.app.Activity
import dev.sotoestevez.allforone.vo.Action
import dev.sotoestevez.allforone.vo.Notification

/** Handler to perform the notifications repository actions with the viewmodel coroutine and network logic
 */
interface ViewModelNotificationsHandler {

    /**
     * Call to retrieve the pending notifications of the user
     *
     * @param callback  Callback to perform with the retrieved notifications
     */
    suspend fun getNotifications(callback: (List<Notification>) -> Unit)

    /**
     * Navigates to the notification defined destiny
     *
     * @param destiny   Destination activity
     */
    fun gotToNotificationDestiny(destiny: Class<out Activity>)

    /**
     * Subscribes the socket to a certain notification and executes the given callback
     *
     * @param callback  Event listener, receives the notification
     */
    fun onNotification(action: Action, callback: (name: Notification) -> Unit)

    /** Sets a notification as read by the user */
    fun setAllAsRead()

    /**
     * Sets a notification as read by the user
     *
     * @param notification    Notification to set as read
     */
    fun setAsRead(notification: Notification)

}