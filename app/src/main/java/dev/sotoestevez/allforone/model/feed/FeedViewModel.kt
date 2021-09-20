package dev.sotoestevez.allforone.model.feed

import androidx.lifecycle.SavedStateHandle
import dev.sotoestevez.allforone.model.ExtendedViewModel
import dev.sotoestevez.allforone.model.PrivateViewModel
import dev.sotoestevez.allforone.repositories.FeedRepository
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.util.dispatcher.DefaultDispatcherProvider
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider

/** ViewModel of the Feed Activity */
class FeedViewModel(
	savedStateHandle: SavedStateHandle,
	dispatchers: DispatcherProvider = DefaultDispatcherProvider,
	sessionRepository: SessionRepository = SessionRepository(),
	private val feedRepository: FeedRepository = FeedRepository()
) : PrivateViewModel(savedStateHandle, dispatchers, sessionRepository) {

	@Suppress("unused") // Used in the factory with a class call
	constructor(builder: ExtendedViewModel.Builder): this(
		builder.savedStateHandle,
		builder.dispatchers,
		builder.sessionRepository,
		builder.feedRepository
	)

	init {
		// retrieve messages from API
		feedRepository.join(user.value!!)
	}

	/**
	 * Sends the message
	 *
	 * @param message content of the message
	 */
	fun sendMessage(message: String) {
		feedRepository.send(user.value!!, message)
	}

}