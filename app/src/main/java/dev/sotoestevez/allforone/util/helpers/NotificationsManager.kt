package dev.sotoestevez.allforone.util.helpers

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import dev.sotoestevez.allforone.BR
import dev.sotoestevez.allforone.ui.components.recyclerview.notifications.NotificationDateHeaderView
import dev.sotoestevez.allforone.ui.components.recyclerview.notifications.NotificationView
import dev.sotoestevez.allforone.ui.components.recyclerview.notifications.NotificationsView
import dev.sotoestevez.allforone.ui.components.recyclerview.notifications.listeners.NotificationListener
import dev.sotoestevez.allforone.vo.Action
import dev.sotoestevez.allforone.vo.Notification
import java.lang.IllegalStateException
import java.util.*

/**
 * Controls the logic of managing the notifications
 *
 * @property handler Object containing the repository functions with viewmodel handling logic
 */
class NotificationsManager(
    private val handler: ViewModelNotificationsHandler
): BaseObservable() {

    /** List of pending notifications to display */
    @get:Bindable
    val notifications: List<NotificationsView>
        get() = mNotifications
    private val mNotifications: LinkedList<NotificationsView> = LinkedList()

    /** Current size of pending notifications */
    @get:Bindable
    val numberOfNotifications: Int
        get() = mNotifications.size - headers

    private var headers: Int = 0

    /** Loads the given list of notifications */
    suspend fun load() {
        clear()
        val notifications = handler.getNotifications()
        val notificationByDate = notifications.sortedByDescending { it.timestamp }.groupBy { TimeFormatter.getDate(it.timestamp) }
        notificationByDate.keys.forEach {
            mNotifications.removeIf { v -> v.id == it }
            mNotifications.add(NotificationDateHeaderView(it)).also { headers++ }
            notificationByDate[it]?.map { m -> NotificationView(m, listener) }?.let { i -> mNotifications.addAll(i) }
        }
        notifyChanges()
    }

    /** Reads all the notifications */
    fun readAll() {
        handler.setAllAsRead()
        clear().also { notifyChanges() }
    }

    /** Subscribes the manager to the notification events */
    fun subscribe() {
        handler.apply {
            onNotification(Action.BOND_CREATED) { add(it) }
            onNotification(Action.TASK_CREATED) { add(it) }
            onNotification(Action.TASK_DELETED) { remove(it.id); add(it) }
            onNotification(Action.TASK_DONE) { add(it) }
            onNotification(Action.TASK_UNDONE) { add(it) }
            onNotification(Action.LOCATION_SHARING_START) { add(it) }
            onNotification(Action.LOCATION_SHARING_STOP) { remove(it.id).also { notifyChanges() } }
        }
    }

    private fun add(notification: Notification) = mNotifications.addFirst(NotificationView(notification, listener)).also { notifyChanges() }

    private fun clear() = mNotifications.clear().also { headers = 0 }

    private fun remove(id: String) = mNotifications.removeIf { it.id == id }

    private fun notifyChanges() {
        notifyPropertyChanged(BR.notifications)
        notifyPropertyChanged(BR.numberOfNotifications)
    }

    private val listener: NotificationListener = object: NotificationListener {

        override fun onGo(notification: Notification) {
            val destiny = notification.action.destiny ?: throw IllegalStateException("Accessing to destiny of invalid action")
            handler.gotToNotificationDestiny(destiny)
        }

        override fun onRead(notification: Notification) {
            handler.setAsRead(notification)
            remove(notification.id).also { notifyChanges() }
        }

    }

}