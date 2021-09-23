package dev.sotoestevez.allforone.repositories.impl

import com.google.gson.Gson
import dev.sotoestevez.allforone.api.SocketManager
import dev.sotoestevez.allforone.api.schemas.GlobalSubscriptionMsg
import dev.sotoestevez.allforone.api.schemas.UserInfoMsg
import dev.sotoestevez.allforone.repositories.GlobalRoomRepository
import dev.sotoestevez.allforone.util.extensions.logDebug

/**
 * Implementation of the [GlobalRoomRepository]
 *
 * @constructor
 *
 * @param gson	JSON serializer/deserializer
 */
class GlobalRoomRepositoryImpl(gson: Gson = Gson()): BaseSocketRepository(gson), GlobalRoomRepository {

	override fun connect(userId: String) {
		socket.on(GlobalRoomRepository.Events.CONNECT.id) {
			logDebug("Socket connected to the server with id[${socket.id()}]")
			socket.emit(GlobalRoomRepository.Events.SUBSCRIBE.id, userId)
		}
		socket.on(GlobalRoomRepository.Events.SUBSCRIPTION.id) {
			val received = fromJson(it, GlobalSubscriptionMsg::class.java)
			logDebug("New subscriber joined the Global Room: ${received.room}")
		}
		SocketManager.start()
	}

	override fun onSharingLocation(callback: (name: String) -> Unit) {
		socket.on(GlobalRoomRepository.Events.SHARING_LOCATION.id) {
			callback(fromJson(it, UserInfoMsg::class.java).displayName)
		}
	}

}