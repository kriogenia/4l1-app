package dev.sotoestevez.allforone.repositories

import com.google.gson.Gson
import dev.sotoestevez.allforone.api.requests.GlobalSubscribe
import dev.sotoestevez.allforone.api.requests.GlobalSubscription
import dev.sotoestevez.allforone.entities.SocketManager
import dev.sotoestevez.allforone.util.extensions.logDebug
import io.socket.client.Socket
import org.json.JSONObject

class SocketRepository(
	val gson: Gson = Gson()
) {

	private val socket: Socket = SocketManager.socket

	private companion object {
		const val CONNECT = "connect"
		const val GLOBAL_SUBSCRIBE = "global:subscribe"
		const val GLOBAL_SUBSCRIPTION = "global:subscription"
	}

	fun connect(userId: String, caredId: String = userId) {
		socket.on(CONNECT) {
			logDebug("Socket connected to the server with id[${socket.id()}]")
			socket.emit(GLOBAL_SUBSCRIBE, toJson(GlobalSubscribe(userId, caredId)))
		}
		socket.on(GLOBAL_SUBSCRIPTION) {
			val received = fromJson(it, GlobalSubscription::class.java)
			logDebug("New subscriber joined the Global Room: id[${received.user}]")
		}
		SocketManager.start()
	}


	private fun toJson(request: Any): JSONObject {
		return JSONObject(Gson().toJson(request))
	}

	private fun <T> fromJson(received: Array<out Any>, type: Class<out T>): T {
		return gson.fromJson(received[0].toString(), type)
	}

}