package dev.sotoestevez.allforone.vo

import android.content.Context
import com.google.gson.annotations.SerializedName

/**
 * Notifications received from the server representing user actions
 *
 * @property id Unique identifier of the notification
 * @property action Action notified
 * @property instigator Display name of the user that made the action
 * @property timestamp Creation timestamp of the action
 */
data class Notification(
    @SerializedName("_id")  val id: String,
    val action: Action,
    val instigator: String,
    val timestamp: Long
) {

    /**
     * Returns the string to print the action
     *
     * @param context   Context to retrieve the string template
     * @return          Printable string
     */
    fun print(context: Context) = action.print(context, instigator)

}