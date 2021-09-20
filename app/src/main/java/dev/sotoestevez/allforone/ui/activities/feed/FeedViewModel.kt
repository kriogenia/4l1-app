package dev.sotoestevez.allforone.ui.activities.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dev.sotoestevez.allforone.vo.Message
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.ui.viewmodel.ExtendedViewModel
import dev.sotoestevez.allforone.ui.viewmodel.PrivateViewModel
import dev.sotoestevez.allforone.ui.components.recyclerview.messages.SentMessageViewHolder
import dev.sotoestevez.allforone.repositories.FeedRepository
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.ui.components.recyclerview.ViewHolderWrapper
import dev.sotoestevez.allforone.util.dispatcher.DefaultDispatcherProvider
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import java.time.Instant

/** ViewModel of the Feed Activity */
class FeedViewModel(
	savedStateHandle: SavedStateHandle,
	dispatchers: DispatcherProvider = DefaultDispatcherProvider,
	sessionRepository: SessionRepository = SessionRepository(),
	private val feedRepository: FeedRepository = FeedRepository()
) : PrivateViewModel(savedStateHandle, dispatchers, sessionRepository) {

	/** LiveData holding the list of messages of the feed */
	val feedList: LiveData<MutableList<ViewHolderWrapper<Message>>>
		get() = mFeedList
	private val mFeedList: MutableLiveData<MutableList<ViewHolderWrapper<Message>>> = MutableLiveData(mutableListOf())

	@Suppress("unused") // Used in the factory with a class call
	constructor(builder: ExtendedViewModel.Builder): this(
		builder.savedStateHandle,
		builder.dispatchers,
		builder.sessionRepository,
		builder.feedRepository
	)

	init {
		// TODO retrieve messages from API
		val items = createViewItems(listOf(
			Message(1, "a", User(user.value!!.id, "", User.Role.PATIENT), Instant.now().toEpochMilli()),
			Message(2, "b", User(user.value!!.id, "", User.Role.KEEPER), Instant.now().toEpochMilli())
		))
		mFeedList.postValue(items.toMutableList())
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

	private fun createViewItems(messages: List<Message>): List<ViewHolderWrapper<Message>> {
		return messages.map {
			if (it.user.id == user.value!!.id) SentMessageViewHolder(it) else SentMessageViewHolder(it)
		}
	}

}