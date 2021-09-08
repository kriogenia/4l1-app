package dev.sotoestevez.allforone.entities

import dev.sotoestevez.allforone.BuildConfig
import dev.sotoestevez.allforone.util.extensions.logDebug
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket

object SocketManager {

	val socket: Socket
		get() = mSocket
	private lateinit var mSocket: Socket

	init {
		val opts = IO.Options()
		opts.transports = arrayOf(WebSocket.NAME)
		mSocket = IO.socket(BuildConfig.SERVER_IP)
	}

	fun start() {
		if (!socket.connected()) {
			socket.connect()        // Send a connect request
		}
	}

}