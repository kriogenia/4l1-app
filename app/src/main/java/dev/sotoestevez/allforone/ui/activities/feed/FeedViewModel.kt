package dev.sotoestevez.allforone.ui.activities.feed

import android.util.Log
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

	/** LiveData holding the last notification to display in the notification label */
	val notification: LiveData<RoomNotification?>
		get() = mNotification
	private val mNotification: MutableLiveData<RoomNotification?> = MutableLiveData(null)

	private var page = 1
	private var loadedAll = false

	@Suppress("unused") // Used in the factory with a class call
	constructor(builder: ExtendedViewModel.Builder): this(
		builder.savedStateHandle,
		builder.dispatchers,
		builder.sessionRepository,
		builder.feedRepository
	)

	init {
		retrieveMessages()
		feedRepository.onUserJoining { mNotification.postValue(UserJoiningNotification(it)) }
		feedRepository.onUserLeaving { mNotification.postValue(UserLeavingNotification(it)) }
		feedRepository.onNewMessage { mList.add(wrapItem(it)).also { mFeedList.apply { postValue(value) } } }
		feedRepository.join(user.value!!)
	}

	@Suppress("KDocMissingDocumentation")
	override fun onCleared() {
		feedRepository.leave(user.value!!)
		super.onCleared()
	}

	// TODO clear keyboard after sending message

	/**
	 * Sends the message
	 *
	 * @param text content of the message to send
	 */
	fun sendMessage(text: String) {
		if (Strings.isEmptyOrWhitespace(text)) return
		feedRepository.send(Message(message = text, user = user.value!!, type = Message.Type.TEXT))
		logDebug("Sent message $text")
	}

	/** Loads more messages into the list*/
	fun addMessages() {
		logDebug("Requested to load more messages")
		if (!loadedAll) retrieveMessages()
	}

	private fun retrieveMessages() {
		if (loading.value!!) return
		loading.value = true
		viewModelScope.launch(dispatchers.io()) {
			val messages = feedRepository.getMessages(page++, authHeader()).sortedBy { it.timestamp }
			if (messages.size < 25) {
				loadedAll = true
				Log.d(FeedViewModel::class.simpleName, "All the room messages already loaded")
			}
			withContext(dispatchers.main()) {
				mList.addAll(messages.map { wrapItem(it) }).also { mFeedList.invoke() }
				loading.value = false
				Log.d(FeedViewModel::class.simpleName, "Retrieved list of ${messages.size} messages")
			}
		}
	}

	private fun wrapItem(message: Message): BindedItemView {
		return if (message.user.id == user.value!!.id) SentMessageTextView(message) else ReceivedTextMessageView(message)
	}

}