package dev.sotoestevez.allforone.ui.activities.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.util.Strings
import dev.sotoestevez.allforone.vo.Message
import dev.sotoestevez.allforone.ui.viewmodel.ExtendedViewModel
import dev.sotoestevez.allforone.ui.viewmodel.PrivateViewModel
import dev.sotoestevez.allforone.ui.components.recyclerview.messages.SentMessageTextView
import dev.sotoestevez.allforone.repositories.FeedRepository
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.ui.components.recyclerview.BindedItemView
import dev.sotoestevez.allforone.ui.components.recyclerview.messages.ReceivedTextMessageView
import dev.sotoestevez.allforone.util.dispatcher.DefaultDispatcherProvider
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import dev.sotoestevez.allforone.util.extensions.invoke
import dev.sotoestevez.allforone.util.extensions.logDebug
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

	private var page = 1

	@Suppress("unused") // Used in the factory with a class call
	constructor(builder: ExtendedViewModel.Builder): this(
		builder.savedStateHandle,
		builder.dispatchers,
		builder.sessionRepository,
		builder.feedRepository
	)

	init {
		loading.value = true
		viewModelScope.launch(dispatchers.io()) {
			val messages = feedRepository.getMessages(page++, authHeader()).sortedBy { it.timestamp }
			withContext(dispatchers.main()) {
				mList.addAll(messages.map { wrapItem(it) }).also { mFeedList.invoke() }
				loading.value = false
			}
		}
		feedRepository.onNewMessage { mList.add(wrapItem(it)).also { mFeedList.apply { postValue(value) } } }
		feedRepository.join(user.value!!)
	}

	// TODO show users joining/leaving the room
	// TODO load more messages
	// TODO clear keyboard after sending message

	/**
	 * Sends the message
	 *
	 * @param text content of the message to send
	 */
	fun sendMessage(text: String) {
		if (Strings.isEmptyOrWhitespace(text)) return
		feedRepository.send(Message(message = text, user = user.value!!, type = Message.Type.TEXT))
	}

	private fun wrapItem(message: Message): BindedItemView {
		return if (message.user.id == user.value!!.id) SentMessageTextView(message) else ReceivedTextMessageView(message)
	}

}