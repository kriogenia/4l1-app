package dev.sotoestevez.allforone.ui.activities.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dev.sotoestevez.allforone.vo.Message
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.ui.viewmodel.ExtendedViewModel
import dev.sotoestevez.allforone.ui.viewmodel.PrivateViewModel
import dev.sotoestevez.allforone.ui.components.recyclerview.messages.SentMessageTextView
import dev.sotoestevez.allforone.repositories.FeedRepository
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.ui.components.recyclerview.BindedItemView
import dev.sotoestevez.allforone.ui.components.recyclerview.messages.ReceivedTextMessageView
import dev.sotoestevez.allforone.util.dispatcher.DefaultDispatcherProvider
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import java.time.Instant

/** ViewModel of the Feed Activity */
class FeedViewModel(
	savedStateHandle: SavedStateHandle,
	dispatchers: DispatcherProvider = DefaultDispatcherProvider,
	sessionRepository: SessionRepository,
	private val feedRepository: FeedRepository
) : PrivateViewModel(savedStateHandle, dispatchers, sessionRepository) {

	/** LiveData holding the list of messages of the feed */
	val feedList: LiveData<List<BindedItemView>>
		get() = mFeedList
	private val mList: MutableList<BindedItemView> = mutableListOf()
	private val mFeedList: MutableLiveData<List<BindedItemView>> = MutableLiveData(mList)

	@Suppress("unused") // Used in the factory with a class call
	constructor(injector: ExtendedViewModel.Injector): this(
		injector.savedStateHandle,
		injector.dispatchers,
		injector.sessionRepository,
		injector.feedRepository
	)

	init {
		feedRepository.onNewMessage { mList.add(wrapItem(it)).also { mFeedList.apply { postValue(value) } } }

		// TODO retrieve messages from API
		mList.addAll(listOf(
			Message("a", "Boas", User(user.value!!.id, "", User.Role.PATIENT), Instant.now().toEpochMilli()),
			Message("b", "Hola", User("b", "", User.Role.KEEPER, "Pepe"), Instant.now().toEpochMilli())
		).map { wrapItem(it) })
		//mFeedList.postValue(mList.toMutableList())
		feedRepository.join(user.value!!)
	}

	/**
	 * Sends the message
	 *
	 * @param text content of the message to send
	 */
	fun sendMessage(text: String) {
		feedRepository.send(Message(message = text, user = user.value!!))
	}

	private fun wrapItem(message: Message): BindedItemView {
		return if (message.user.id == user.value!!.id) SentMessageTextView(message) else ReceivedTextMessageView(message)
	}

}