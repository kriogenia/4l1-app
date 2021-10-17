package dev.sotoestevez.allforone.util.helpers

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import dev.sotoestevez.allforone.BR
import dev.sotoestevez.allforone.repositories.GlobalRoomRepository
import dev.sotoestevez.allforone.ui.components.recyclerview.notifications.NotificationView
import dev.sotoestevez.allforone.vo.Action
import dev.sotoestevez.allforone.vo.Notification

/** Controls the logic of managing the incoming notifications */
class NotificationsManager: BaseObservable() {

    /** List of pending notifications to display */
    @get:Bindable
    val notifications: List<NotificationView>
        get() = mNotifications
    private val mNotifications: MutableList<NotificationView> = mutableListOf()

    /** Current size of pending notifications */
    @get:Bindable
    val size: String
        get() = mNotifications.size.toString()

    /**
     * Subscribes the manager to the notification events
     *
     * @param repository    Global repository sending the notifications
     */
    fun subscribe(repository: GlobalRoomRepository) {
        repository.apply {
            onNotification(Action.BOND_CREATED) { add(it) }
            onNotification(Action.TASK_CREATED) { add(it) }
            onNotification(Action.TASK_DELETED) { add(it) }
            onNotification(Action.TASK_DONE) { add(it) }
            onNotification(Action.TASK_UNDONE) { add(it) }
            onNotification(Action.LOCATION_SHARING_START) { add(it) }
            onNotification(Action.LOCATION_SHARING_STOP) { add(it) }
        }
    }

    private fun add(notification: Notification) {
        mNotifications.add(NotificationView(notification))
        notifyPropertyChanged(BR.notifications)
        notifyPropertyChanged(BR.size)
    }

}