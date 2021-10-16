package dev.sotoestevez.allforone.repositories.impl

import com.google.gson.Gson
import dev.sotoestevez.allforone.api.SocketManager
import dev.sotoestevez.allforone.api.schemas.GlobalSubscriptionMsg
import dev.sotoestevez.allforone.api.schemas.UserInfoMsg
import dev.sotoestevez.allforone.repositories.GlobalRoomRepository
import dev.sotoestevez.allforone.util.extensions.logDebug
import dev.sotoestevez.allforone.vo.Action
import dev.sotoestevez.allforone.vo.Notification
import dev.sotoestevez.allforone.vo.User

/**
 * Implementation of the [GlobalRoomRepository]
 *
 * @constructor
 *
 * @param gson	JSON serializer/deserializer
 */
class GlobalRoomRepositoryImpl(gson: Gson = Gson()): BaseSocketRepository(gson), GlobalRoomRepository {

	/** Events managed by the Global Room Repository **/
	enum class Events(internal val path: String) {
		/** Event sent to the socket when it's successfully connected */
		CONNECT("connect"),
		/** Event to subscribe to the global room*/
		SUBSCRIBE("global:subscribe"),
		/** Even sent to the socket when a subscription to the global room is confirmed */
		SUBSCRIPTION("global:subscription")
	}

	override fun join(user: User) {
		socket.on(Events.CONNECT.path) {
			logDebug("Socket connected to the server with id[${socket.id()}]")
			socket.emit(Events.SUBSCRIBE.path, user.id)
		}
		socket.on(Events.SUBSCRIPTION.path) {
			val received = fromJson(it, GlobalSubscriptionMsg::class.java)
			logDebug("New subscriber joined the Global Room: ${received.room}")
		}
		SocketManager.start()
	}

	override fun leave(user: User) {
		throw NotImplementedError()	// TODO
	}

	override fun onSharingLocation(callback: (Notification) -> Unit) {
		socket.on(Action.LOCATION_SHARING_START.path) {
			callback(fromJson(it, Notification::class.java))
		}
	}

}