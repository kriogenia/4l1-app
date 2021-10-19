package dev.sotoestevez.allforone.util.helpers

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import dev.sotoestevez.allforone.BR
import dev.sotoestevez.allforone.repositories.GlobalRoomRepository
import dev.sotoestevez.allforone.repositories.NotificationRepository
import dev.sotoestevez.allforone.ui.components.recyclerview.feed.FeedView
import dev.sotoestevez.allforone.ui.components.recyclerview.feed.TextHeaderView
import dev.sotoestevez.allforone.ui.components.recyclerview.notifications.NotificationDateHeaderView
import dev.sotoestevez.allforone.ui.components.recyclerview.notifications.NotificationView
import dev.sotoestevez.allforone.ui.components.recyclerview.notifications.NotificationsView
import dev.sotoestevez.allforone.util.extensions.logDebug
import dev.sotoestevez.allforone.vo.Action
import dev.sotoestevez.allforone.vo.Notification
import dev.sotoestevez.allforone.vo.feed.Message
import java.util.*

/** Controls the logic of managing the incoming notifications */
class NotificationsManager: BaseObservable() {

    /** List of pending notifications to display */
    @get:Bindable
    val notifications: List<NotificationsView>
        get() = mNotifications
    private val mNotifications: LinkedList<NotificationsView> = LinkedList()

    /** Current size of pending notifications */
    @get:Bindable
    val numberOfNotifications: Int
        get() = mNotifications.size

    /**
     * Loads the given list of notifications
     *
     * @param notifications Notifications to load
     */
    fun load(notifications: List<Notification>) {
        mNotifications.clear()
        val notificationByDate = notifications.sortedByDescending { it.timestamp }.groupBy { TimeFormatter.getDate(it.timestamp) }
        notificationByDate.keys.forEach {
            mNotifications.removeIf { v -> v.id == it }
            mNotifications.add(NotificationDateHeaderView(it))
            notificationByDate[it]?.map { m -> NotificationView(m) }?.let { i -> mNotifications.addAll(i) }
        }
        reload()
    }

    /**
     * Subscribes the manager to the notification events
     *
     * @param repository    Global repository sending the notifications
     */
    fun subscribe(repository: NotificationRepository) {
        repository.apply {
            onNotification(Action.BOND_CREATED) { add(it) }
            onNotification(Action.TASK_CREATED) { add(it) }
            onNotification(Action.TASK_DELETED) { remove(it.id); add(it) }
            onNotification(Action.TASK_DONE) { add(it) }
            onNotification(Action.TASK_UNDONE) { add(it) }
            onNotification(Action.LOCATION_SHARING_START) { add(it) }
            onNotification(Action.LOCATION_SHARING_STOP) { remove(it.id).also { reload() } }
        }
    }

    private fun add(notification: Notification) = mNotifications.addFirst(NotificationView(notification)).also { reload() }

    private fun remove(id: String) = mNotifications.removeIf { it.id == id }

    private fun reload() {
        notifyPropertyChanged(BR.notifications)
        notifyPropertyChanged(BR.numberOfNotifications)
    }

}