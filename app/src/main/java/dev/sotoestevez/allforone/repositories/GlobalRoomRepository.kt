package dev.sotoestevez.allforone.repositories

import com.google.gson.Gson
import dev.sotoestevez.allforone.api.requests.GlobalSubscribe
import dev.sotoestevez.allforone.api.requests.RoomSubscription
import dev.sotoestevez.allforone.entities.SocketManager
import dev.sotoestevez.allforone.util.extensions.logDebug
import io.socket.client.Socket
import org.json.JSONObject

/** Repository in charge of the Global Room connections and events */
class GlobalRoomRepository(
	private val gson: Gson = Gson()
) {

	private val socket: Socket = SocketManager.socket

	/**
	 * Events managed by the Global Room Repository
	 **/
	enum class Events(internal val id: String) {
		CONNECT("connect"),
		SUBSCRIBE("global:subscribe"),
		SUBSCRIPTION("global:subscription"),
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
			socket.emit(Events.SUBSCRIBE.id, toJson(GlobalSubscribe(userId, caredId)))
		}
		socket.on(Events.SUBSCRIPTION.id) {
			val received = fromJson(it, RoomSubscription::class.java)
			logDebug("New subscriber joined the Global Room: ${received.displayName}")
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
			callback(fromJson(it, RoomSubscription::class.java).displayName)
		}
	}

	private fun toJson(request: Any): JSONObject {
		return JSONObject(Gson().toJson(request))
	}

	private fun <T> fromJson(received: Array<out Any>, type: Class<out T>): T {
		return gson.fromJson(received[0].toString(), type)
	}

}