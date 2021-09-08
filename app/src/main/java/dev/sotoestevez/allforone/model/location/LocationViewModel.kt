package dev.sotoestevez.allforone.model.location

import androidx.lifecycle.SavedStateHandle
import dev.sotoestevez.allforone.entities.SocketManager
import dev.sotoestevez.allforone.model.PrivateViewModel
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import dev.sotoestevez.allforone.util.extensions.logDebug
import java.net.URISyntaxException

class LocationViewModel(
	savedStateHandle: SavedStateHandle,
	dispatchers: DispatcherProvider,
	sessionRepository: SessionRepository = SessionRepository()
): PrivateViewModel(savedStateHandle, dispatchers, sessionRepository)/*, WithSocket*/ {
/*
	override lateinit var socket: Socket

	init { initSocket() }*/

	init {
		logDebug(SocketManager.socket.id())
	}

}