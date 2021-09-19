package dev.sotoestevez.allforone.repositories

import com.google.gson.Gson
import dev.sotoestevez.allforone.api.schemas.GlobalSubscribeMsg
import dev.sotoestevez.allforone.api.schemas.GlobalSubscriptionMsg
import dev.sotoestevez.allforone.api.schemas.UserInfoMsg
import dev.sotoestevez.allforone.entities.SocketManager
import dev.sotoestevez.allforone.util.extensions.logDebug

/**
 * Repository in charge of the Global Room connections and events
 *
 * @constructor
 *
 * @param gson   Gson serializer/deserializer
 * */
class GlobalRoomRepository(gson: Gson = Gson()): BaseSocketRepository(gson) {

	/** Events managed by the Global Room Repository **/
	enum class Events(internal val id: String) {
		/** Event sent to the socket when it's successfully connected */
		CONNECT("connect"),
		/** Event to subscribe to the global room*/
		SUBSCRIBE("global:subscribe"),
		/** Even sent to the socket when a subscription to the global room is confirmed */
		SUBSCRIPTION("global:subscription"),
		/** Event sent to the socket when a user of the same global room starts sharing its location */
		SHARING_LOCATION("global:sharing_location")
	}

	/**
	 * Connects the socket of the application to the server and launches a subscription to the assigned global room
	 *
	 * @param userId    id of the current User
	 * @param caredId   id of the cared User if the current User is a Keeper, otherwise it's the same as the [userId]
	 */
	fun connect(userId: String, caredId: String = userId) {
		socket.on(Events.CONNECT.id) {
			logDebug("Socket connected to the server with id[${socket.id()}]")
			socket.emit(Events.SUBSCRIBE.id, toJson(GlobalSubscribeMsg(userId, caredId)))
		}
		socket.on(Events.SUBSCRIPTION.id) {
			val received = fromJson(it, GlobalSubscriptionMsg::class.java)
			logDebug("New subscriber joined the Global Room: ${received.room}")
		}
		SocketManager.start()
	}

	/**
	 * Subscribes the socket to the event thrown when a Bond starts sharing its location
	 *
	 * @param callback  Event listener, receives the name of the subscribed user
	 */
	fun onSharingLocation(callback: (name: String) -> Unit) {
		socket.on(Events.SHARING_LOCATION.id) {
			callback(fromJson(it, UserInfoMsg::class.java).displayName)
		}
	}

}