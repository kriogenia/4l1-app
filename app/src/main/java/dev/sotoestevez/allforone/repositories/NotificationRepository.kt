package dev.sotoestevez.allforone.repositories

import dev.sotoestevez.allforone.repositories.impl.BaseSocketRepository
import dev.sotoestevez.allforone.vo.Action
import dev.sotoestevez.allforone.vo.Notification

/** Repository in charge of the notifications */
interface NotificationRepository {

	/**
	 * Retrieves the pending notifications
	 *
	 * @param maxDays	Limit of days of the notification
	 * @param token 	Authentication token
	 */
	suspend fun getNotifications(token: String): List<Notification>

	/**
	 * Subscribes the socket to a certain notification and executes the given callback
	 *
	 * @param callback  Event listener, receives the notification
	 */
	fun onNotification(action: Action, callback: (name: Notification) -> Unit)

}