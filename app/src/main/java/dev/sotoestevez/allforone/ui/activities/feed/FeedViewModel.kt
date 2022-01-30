package dev.sotoestevez.allforone.ui.activities.feed

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.util.Strings
import dev.sotoestevez.allforone.repositories.*
import dev.sotoestevez.allforone.vo.feed.TextMessage
import dev.sotoestevez.allforone.ui.viewmodel.ExtendedViewModel
import dev.sotoestevez.allforone.ui.viewmodel.PrivateViewModel
import dev.sotoestevez.allforone.ui.components.exchange.dialog.DeleteTaskConfirmation
import dev.sotoestevez.allforone.ui.components.exchange.dialog.DialogConfirmationRequest
import dev.sotoestevez.allforone.ui.components.exchange.dialog.SetTaskDoneConfirmation
import dev.sotoestevez.allforone.ui.components.exchange.notification.NewMessageNotification
import dev.sotoestevez.allforone.ui.components.exchange.notification.TextNotification
import dev.sotoestevez.allforone.ui.components.exchange.notification.UserJoiningNotification
import dev.sotoestevez.allforone.ui.components.exchange.notification.UserLeavingNotification
import dev.sotoestevez.allforone.ui.components.recyclerview.BindedItemView
import dev.sotoestevez.allforone.ui.components.recyclerview.feed.*
import dev.sotoestevez.allforone.ui.components.recyclerview.tasks.TaskView
import dev.sotoestevez.allforone.ui.components.recyclerview.tasks.listeners.TaskListener
import dev.sotoestevez.allforone.util.dispatcher.DefaultDispatcherProvider
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import dev.sotoestevez.allforone.util.extensions.invoke
import dev.sotoestevez.allforone.util.extensions.logDebug
import dev.sotoestevez.allforone.util.helpers.TimeFormatter
import dev.sotoestevez.allforone.vo.notification.Action
import dev.sotoestevez.allforone.vo.notification.Notification
import dev.sotoestevez.allforone.vo.Task
import dev.sotoestevez.allforone.vo.User
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
    private val notificationRepository: NotificationRepository,
    private val feedRepository: FeedRepository,
    private val taskRepository: TaskRepository
) : PrivateViewModel(savedStateHandle, dispatchers, sessionRepository) {

    /** LiveData holding the list of messages of the feed */
    val feedList: LiveData<List<BindedItemView>>
        get() = mFeedList
    private val mList: LinkedList<FeedView> = LinkedList()
    private val mFeedList: MutableLiveData<List<BindedItemView>> = MutableLiveData(mList)

    /** LiveData holding the last notification to display in the notification label */
    val notification: LiveData<TextNotification?>
        get() = mNotification
    private val mNotification: MutableLiveData<TextNotification?> = MutableLiveData(null)

    /** LiveData holding the last task changed */
    val actionTaskToConfirm: LiveData<DialogConfirmationRequest>
        get() = mActionTaskToConfirm
    private val mActionTaskToConfirm: MutableLiveData<DialogConfirmationRequest> = MutableLiveData()

    /** Mutable implementation of the error live data exposed **/
    override val error: LiveData<Throwable>
        get() = mError
    private var mError = MutableLiveData<Throwable>()

    /** LiveData with the flag indicating if the activity is in task mode **/
    val taskMode: MutableLiveData<Boolean> = MutableLiveData(false)

    private var page = 1
    private var loadedAll = false

    @Suppress("unused") // Used in the factory with a class call
    constructor(builder: ExtendedViewModel.Builder) : this(
        builder.savedStateHandle,
        builder.dispatchers,
        builder.sessionRepository,
        builder.notificationRepository,
        builder.feedRepository,
        builder.taskRepository
    )

    init {
        retrieveMessages()
        subscribe()
        feedRepository.join(user.value!!)
    }

    @Suppress("KDocMissingDocumentation")
    override fun onCleared() {
        feedRepository.leave(user.value!!)
        super.onCleared()
    }

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
     * @param title        Title of the task
     * @param description    Description of the task
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

    private fun subscribe() {
        notificationRepository.onNotification(Action.TASK_DELETED) { showNotification(it) }
        notificationRepository.onNotification(Action.TASK_DONE) { onTaskStateUpdate(it, true) }
        notificationRepository.onNotification(Action.TASK_UNDONE) { onTaskStateUpdate(it, false) }
        feedRepository.onUserJoining { mNotification.postValue(UserJoiningNotification(it)) }
        feedRepository.onUserLeaving { mNotification.postValue(UserLeavingNotification(it)) }
        feedRepository.onNewMessage { onNewMessage(it) }
        feedRepository.onMessageDeleted { onMessageRemoval(it) }
    }

    private fun onNewMessage(message: Message) {
        mList.add(wrapItem(message)).also { mFeedList.apply { postValue(value) } }
        mNotification.postValue(NewMessageNotification(message.submitter.displayName!!))
    }

    private fun onMessageRemoval(message: Message) {
        mList.removeIf { it.id == message.id }
        mFeedList.apply { postValue(value) }
    }

    private fun showNotification(notification: Notification) {
        mList.add(FeedNotificationView(notification))
        mFeedList.apply { postValue(value) }
    }

    private fun onTaskStateUpdate(notification: Notification, done: Boolean) {
        mList.find { it.id == notification.tags[1] }.run {
            if (this !is TaskMessageView) return@run
            this.updateState(done)
        }
        showNotification(notification)
    }

    private fun wrapList(messages: List<Message>): List<FeedView> {
        val items: MutableList<FeedView> = mutableListOf()
        val messagesByDate = messages.groupBy { TimeFormatter.getDate(it.timestamp) }
        messagesByDate.keys.forEach {
            mList.removeIf { v -> v.id == it }
            items.add(TextHeaderView(it))
            messagesByDate[it]?.map { m -> wrapItem(m) }?.let { i -> items.addAll(i) }
        }
        return items
    }

    private fun wrapItem(message: Message): FeedView {
        val sent = (message.submitter.id == user.value!!.id)
        return if (message is TextMessage)
            if (sent) SentTextMessageView(message) else ReceivedTextMessageView(message)
        else if (message is TaskMessage)
            if (sent) SentTaskMessageView(message, taskListener) else ReceivedTaskMessageView(message, taskListener)
        else throw IllegalStateException("Invalid message type")
    }

    private val taskListener: TaskListener = object : TaskListener {
        override fun onChangeDone(view: TaskView) {
            mActionTaskToConfirm.value = SetTaskDoneConfirmation(view) {
                it.swapState()
                viewModelScope.launch(dispatchers.io()) { taskRepository.updateDone(it.data, authHeader()) }
                logDebug("Changed the state of Task[${it.data.id}] to ${it.done}")
            }
        }

        override fun onDelete(view: TaskView) {
            if (user.value!!.role != User.Role.PATIENT && view.data.submitter != user.value) {
                mError.value = IllegalAccessException("Only the task submitter or the patient can delete this task")
                return
            }
            mActionTaskToConfirm.value = DeleteTaskConfirmation(view) {
                viewModelScope.launch(dispatchers.io()) { taskRepository.delete(it.data, authHeader()) }
                mList.removeIf { v -> v.id === it.id }.also { mFeedList.invoke() }
                logDebug("Deleted the Task[${it.id}]")
            }
        }
    }

}