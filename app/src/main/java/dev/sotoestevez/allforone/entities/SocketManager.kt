package dev.sotoestevez.allforone.entities

import dev.sotoestevez.allforone.BuildConfig
import dev.sotoestevez.allforone.util.extensions.logDebug
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket

/** Singleton class to hold the socket connection and make it available to any activity */
object SocketManager {

	/** Subscription to the Global room */
	const val GLOBAL_SUBSCRIBE: String = "global:subscribe"

	/** Socket.io socket */
	val socket: Socket
		get() = mSocket
	private lateinit var mSocket: Socket

	init {
		val opts = IO.Options()
		opts.transports = arrayOf(WebSocket.NAME)
		mSocket = IO.socket(BuildConfig.SERVER_IP)
	}

	/** Sends a connection request if the socket is not yet connected */
	fun start() {
		if (!socket.connected()) {
			socket.connect()        // Send a connect request
		}
	}

}