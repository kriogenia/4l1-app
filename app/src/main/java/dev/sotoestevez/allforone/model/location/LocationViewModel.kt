package dev.sotoestevez.allforone.model.location

import androidx.lifecycle.SavedStateHandle
import dev.sotoestevez.allforone.model.ExtendedViewModel
import dev.sotoestevez.allforone.model.PrivateViewModel
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider

class LocationViewModel(
	savedStateHandle: SavedStateHandle,
	dispatchers: DispatcherProvider,
	sessionRepository: SessionRepository = SessionRepository()
): PrivateViewModel(savedStateHandle, dispatchers, sessionRepository) {

	constructor(builder: ExtendedViewModel.Builder): this(
		builder.savedStateHandle,
		builder.dispatchers,
		builder.sessionRepository
	)

}