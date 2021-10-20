package dev.sotoestevez.allforone.repositories

import dev.sotoestevez.allforone.vo.Action
import dev.sotoestevez.allforone.vo.Notification

/** Repository in charge of the notifications */
interface NotificationRepository {

	/**
	 * Retrieves the pending notifications
	 *
	 * @param token 	Authentication token
	 */
	suspend fun getNotifications(token: String): List<Notification>

	/**
	 * Subscribes the socket to a certain notification and executes the given callback
	 *
	 * @param callback  Event listener, receives the notification
	 */
	fun onNotification(action: Action, callback: (name: Notification) -> Unit)

	/**
	 * Sets a notification as read by the user
	 *
	 * @param notification	Notification to set as read
	 * @param token			Authentication token
	 */
	suspend fun setAsRead(notification: Notification, token: String)

}