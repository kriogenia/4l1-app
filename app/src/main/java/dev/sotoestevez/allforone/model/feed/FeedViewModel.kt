package dev.sotoestevez.allforone.model.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dev.sotoestevez.allforone.data.Message
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.model.ExtendedViewModel
import dev.sotoestevez.allforone.model.PrivateViewModel
import dev.sotoestevez.allforone.model.feed.viewitems.SentMessageViewItem
import dev.sotoestevez.allforone.repositories.FeedRepository
import dev.sotoestevez.allforone.repositories.SessionRepository
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
	val feedList: LiveData<MutableList<MessageViewItem>>
		get() = mFeedList
	private val mFeedList: MutableLiveData<MutableList<MessageViewItem>> = MutableLiveData(mutableListOf())

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

	private fun createViewItems(messages: List<Message>): List<MessageViewItem> {
		return messages.map {
			if (it.user.id == user.value!!.id) SentMessageViewItem(it) else SentMessageViewItem(it)
		}
	}

}