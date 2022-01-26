package dev.sotoestevez.allforone.ui.viewmodel

import android.app.Activity
import dev.sotoestevez.allforone.repositories.NotificationRepository

/** ViewModel of an Activity featuring a notifications management */
interface WithNotifications {

    /** Notifications repository used by the ViewModel */
    val notificationRepository: NotificationRepository

    /** Call to perform an activity navigation */
    fun setDestiny(destiny: Class<out Activity>)

    /** Request for ViewModel to run an asynchronous notification request */
    fun runRequest(request: suspend (String) -> Unit)

}