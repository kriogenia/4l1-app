package dev.sotoestevez.allforone.api

import dev.sotoestevez.allforone.BuildConfig
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import java.lang.IllegalStateException

/** Singleton class to hold the socket connection and make it available to any activity */
object SocketManager {

	private const val DISCONNECT = "disconnect"

	/** Socket.io socket */
	val socket: Socket
		get() = mSocket
	private var mSocket: Socket

	init {
		val opts = IO.Options()
		opts.transports = arrayOf(WebSocket.NAME)
		mSocket = IO.socket(BuildConfig.SERVER_IP)
	}

	/** Sends a connection request if the socket is not yet connected */
	fun start() {
		socket.on(DISCONNECT) {
			// if the user is disconnected from the server the app can't be used and it should be closed
			throw IllegalStateException("The user has been disconnected from the socket")
		}
		if (!socket.connected()) {
			socket.connect()        // Send a connect request
		}
	}

}