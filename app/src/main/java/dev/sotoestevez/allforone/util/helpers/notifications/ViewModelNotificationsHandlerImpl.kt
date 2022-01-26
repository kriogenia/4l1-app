package dev.sotoestevez.allforone.util.helpers.notifications

import android.app.Activity
import dev.sotoestevez.allforone.ui.viewmodel.WithNotifications
import dev.sotoestevez.allforone.vo.Action
import dev.sotoestevez.allforone.vo.Notification

/**
 * Default implementation of the [ViewModelNotificationsHandler]
 *
 * @property model  model to manage the requests and operations
 */
class ViewModelNotificationsHandlerImpl(private val model: WithNotifications) : ViewModelNotificationsHandler {

    private val repository = model.notificationRepository

    override suspend fun getNotifications(callback: (List<Notification>) -> Unit) =
        model.runRequest { callback(repository.getNotifications(it)) }

    override fun gotToNotificationDestiny(destiny: Class<out Activity>) =
        model.setDestiny(destiny)

    override fun onNotification(action: Action, callback: (name: Notification) -> Unit) =
        repository.onNotification(action, callback)

    override fun setAllAsRead() =
        model.runRequest { repository.setAllAsRead(it) }

    override fun setAsRead(notification: Notification) =
        model.runRequest { repository.setAsRead(notification, it) }

}