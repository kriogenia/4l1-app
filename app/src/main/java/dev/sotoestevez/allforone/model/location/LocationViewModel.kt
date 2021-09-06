package dev.sotoestevez.allforone.model.location

import androidx.lifecycle.SavedStateHandle
import dev.sotoestevez.allforone.BuildConfig
import dev.sotoestevez.allforone.model.PrivateViewModel
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import dev.sotoestevez.allforone.util.extensions.logError
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class LocationViewModel(
	savedStateHandle: SavedStateHandle,
	dispatchers: DispatcherProvider,
	sessionRepository: SessionRepository = SessionRepository()
): PrivateViewModel(savedStateHandle, dispatchers, sessionRepository) {

	private lateinit var socket: Socket

	init {
		try {
			socket = IO.socket(BuildConfig.SERVER_IP)       // Retrieve or create a new socket to the server
			socket.connect()                                // Send a connect request
		} catch (e: URISyntaxException) {
			e.message?.let { logError(it) }
		}
	}

}