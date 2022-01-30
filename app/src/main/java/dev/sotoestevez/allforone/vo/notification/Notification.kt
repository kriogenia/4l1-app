package dev.sotoestevez.allforone.vo.notification

import android.content.Context
import androidx.core.app.NotificationCompat
import com.google.gson.annotations.SerializedName
import dev.sotoestevez.allforone.R

/**
 * Notifications received from the server representing user actions
 *
 * @property id Unique identifier of the notification
 * @property action Action notified
 * @property instigator Display name of the user that made the action
 * @property timestamp Creation timestamp of the action
 * @property tags List of tags of the notification
 */
data class Notification(
    @SerializedName("_id") val id: String,
    val action: Action,
    val instigator: String,
    val timestamp: Long,
    val tags: Array<String> = arrayOf()
) {

    /**
     * Returns the string to print the action
     *
     * @param context   Context to retrieve the string template
     * @return          Printable string
     */
    fun print(context: Context) = action.print(context, instigator, *tags)

    /**
     * Builds an Android notification with the info of this notification
     *
     * @param context   Context to retrieve the notification strings
     * @return          Android notification
     */
    fun build(context: Context)  = NotificationCompat.Builder(context, action.channel.id)
        .setSmallIcon(R.drawable.ic_app_complete)
        .setContentTitle(context.getString(action.contentTitle))
        .setContentText(print(context))
        .build()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Notification

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}