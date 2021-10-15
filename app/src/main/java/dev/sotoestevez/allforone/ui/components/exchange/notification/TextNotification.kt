package dev.sotoestevez.allforone.ui.components.exchange.notification

import android.content.Context

/** Model for show notifications in the activity */
sealed interface TextNotification {

    /**
     * Generates the notification full string
     *
     * @param context   Context to load the string resource
     */
    fun getString(context: Context): String

}