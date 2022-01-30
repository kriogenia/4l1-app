package dev.sotoestevez.allforone.util.helpers.notifications

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import dev.sotoestevez.allforone.BR
import dev.sotoestevez.allforone.ui.components.recyclerview.notifications.NotificationDateHeaderView
import dev.sotoestevez.allforone.ui.components.recyclerview.notifications.NotificationView
import dev.sotoestevez.allforone.ui.components.recyclerview.notifications.NotificationsView
import dev.sotoestevez.allforone.ui.components.recyclerview.notifications.listeners.NotificationListener
import dev.sotoestevez.allforone.util.helpers.TimeFormatter
import dev.sotoestevez.allforone.vo.notification.Action
import dev.sotoestevez.allforone.vo.notification.Channel
import dev.sotoestevez.allforone.vo.notification.Notification
import java.util.*

/**
 * Controls the logic of managing the notifications
 *
 * @property handler Object containing the repository functions with viewmodel handling logic
 */
class NotificationsManager(
    private val handler: ViewModelNotificationsHandler,
    private val context: Context
) : BaseObservable() {

    private var manager = NotificationManagerCompat.from(context)

    init {
        // Start notification channels
        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).apply {
            createNotificationChannel(Channel.BOND.build(context))
            createNotificationChannel(Channel.LOCATION.build(context))
            createNotificationChannel(Channel.TASK.build(context))
        }
    }

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
        handler.getNotifications { addAll(it) }
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
            onNotification(Action.BOND_DELETED) { add(it) }
            onNotification(Action.TASK_CREATED) { add(it) }
            onNotification(Action.TASK_DELETED) { remove(it.id); add(it) }
            onNotification(Action.TASK_DONE) { add(it) }
            onNotification(Action.TASK_UNDONE) { add(it) }
            onNotification(Action.LOCATION_SHARING_START) { add(it) }
            onNotification(Action.LOCATION_SHARING_STOP) { remove(it.id).also { notifyChanges() } }
        }
    }

    private fun add(notification: Notification) {
        // Show it
        manager.notify(notification.id.hashCode(), notification.build(context))
        // Store it
        mNotifications.addFirst(NotificationView(notification, listener)).also { notifyChanges() }
    }

    private fun addAll(notifications: List<Notification>) {
        val notificationByDate = notifications.sortedByDescending { it.timestamp }.groupBy { TimeFormatter.getDate(it.timestamp) }
        notificationByDate.keys.forEach {
            mNotifications.removeIf { v -> v.id == it }
            mNotifications.add(NotificationDateHeaderView(it)).also { headers++ }
            notificationByDate[it]?.map { m -> NotificationView(m, listener) }?.let { i -> mNotifications.addAll(i) }
        }
        notifyChanges()
    }

    private fun clear() = mNotifications.clear().also { headers = 0 }

    private fun remove(id: String) = mNotifications.removeIf { it.id == id }

    private fun notifyChanges() {
        notifyPropertyChanged(BR.notifications)
        notifyPropertyChanged(BR.numberOfNotifications)
    }

    private val listener: NotificationListener = object : NotificationListener {

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