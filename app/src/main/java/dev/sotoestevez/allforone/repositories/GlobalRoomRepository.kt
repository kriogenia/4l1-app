package dev.sotoestevez.allforone.repositories

import dev.sotoestevez.allforone.vo.Action
import dev.sotoestevez.allforone.vo.Notification

/** Repository in charge of the Global Room connections and events */
interface GlobalRoomRepository: SocketRepository {

	/**
	 * Subscribes the socket to a certain notification and executes the given callback
	 *
	 * @param callback  Event listener, receives the notification
	 */
	fun onNotification(action: Action, callback: (name: Notification) -> Unit)

}