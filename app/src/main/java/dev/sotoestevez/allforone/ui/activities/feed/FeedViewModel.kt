package dev.sotoestevez.allforone.ui.activities.feed

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.util.Strings
import dev.sotoestevez.allforone.vo.feed.TextMessage
import dev.sotoestevez.allforone.ui.viewmodel.ExtendedViewModel
import dev.sotoestevez.allforone.ui.viewmodel.PrivateViewModel
import dev.sotoestevez.allforone.repositories.FeedRepository
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.ui.components.exchange.notification.NewMessageNotification
import dev.sotoestevez.allforone.ui.components.exchange.notification.TextNotification
import dev.sotoestevez.allforone.ui.components.exchange.notification.UserJoiningNotification
import dev.sotoestevez.allforone.ui.components.exchange.notification.UserLeavingNotification
import dev.sotoestevez.allforone.ui.components.recyclerview.BindedItemView
import dev.sotoestevez.allforone.ui.components.recyclerview.feed.*
import dev.sotoestevez.allforone.util.dispatcher.DefaultDispatcherProvider
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import dev.sotoestevez.allforone.util.extensions.invoke
import dev.sotoestevez.allforone.util.extensions.logDebug
import dev.sotoestevez.allforone.util.helpers.TimeFormatter
import dev.sotoestevez.allforone.vo.Task
import dev.sotoestevez.allforone.vo.feed.Message
import dev.sotoestevez.allforone.vo.feed.TaskMessage
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalStateException
import java.util.*

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
	private val mList: LinkedList<BindedItemView> = LinkedList()
	private val mFeedList: MutableLiveData<List<BindedItemView>> = MutableLiveData(mList)

	/** LiveData holding the last notification to display in the notification label */
	val notification: LiveData<TextNotification?>
		get() = mNotification
	private val mNotification: MutableLiveData<TextNotification?> = MutableLiveData(null)

	/** LiveData with the flag indicating if the activity is in task mode **/
	val taskMode: MutableLiveData<Boolean> = MutableLiveData(false)

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
		feedRepository.onNewMessage { onNewMessage(it) }
		feedRepository.join(user.value!!)
	}

	@Suppress("KDocMissingDocumentation")
	override fun onCleared() {
		feedRepository.leave(user.value!!)
		super.onCleared()
	}

	// TODO clear keyboard after sending message
	// TODO add day header -> group by -> create bonditemview

	/**
	 * Sends a text message
	 *
	 * @param text content of the message to send
	 */
	fun sendTextMessage(text: String) {
		if (Strings.isEmptyOrWhitespace(text)) return
		feedRepository.send(TextMessage(message = text, submitter = user.value!!))
		logDebug("Sent message $text")
	}

	/**
	 * Sends a task message
	 *
	 * @param title 		Title of the task
	 * @param description	Description of the task
	 */
	fun sendTaskMessage(title: String, description: String) {
		feedRepository.send(TaskMessage(Task(title = title, description = description, submitter = user.value!!)))
		logDebug("Sent task $title")
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
				mList.addAll(0, wrapList(messages)).also { mFeedList.invoke() }
				loading.value = false
				Log.d(FeedViewModel::class.simpleName, "Retrieved list of ${messages.size} messages")
			}
		}
	}

	private fun onNewMessage(message: Message) {
		mList.add(wrapItem(message)).also { mFeedList.apply { postValue(value) } }
		mNotification.postValue(NewMessageNotification(message.submitter.displayName!!))
	}

	// TODO remove duplicated date headers

	private fun wrapList(messages: List<Message>): List<BindedItemView> {
		val items: MutableList<BindedItemView> = mutableListOf()
		val messagesByDate = messages.groupBy { TimeFormatter.getDate(it.timestamp) }
		messagesByDate.keys.forEach { it ->
			items.add(DateHeaderView(it))
			messagesByDate[it]?.map { m -> wrapItem(m) }?.let { i -> items.addAll(i) }
		}
		return items
	}

	private fun wrapItem(message: Message): BindedItemView {
		val sent = (message.submitter.id == user.value!!.id)
		return 	if (message is TextMessage)
					if (sent) SentTextMessageView(message) else ReceivedTextMessageView(message)
				else if (message is TaskMessage)
					if (sent) SentTaskMessageView(message) else ReceivedTaskMessageView(message)
				else throw IllegalStateException("Invalid message type")
	}

}