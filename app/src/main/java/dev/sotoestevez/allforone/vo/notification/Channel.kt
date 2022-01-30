package dev.sotoestevez.allforone.vo.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import dev.sotoestevez.allforone.R

/**
 * Enumeration of the notification channels
 *
 * @property id             Channel ID
 * @property title          Name of the channel
 * @property description    Brief description
 */
enum class Channel(
    val id: String,
    private val title: Int,
    private val description: Int,
    private val importance: Int
) {

    BOND(
        "BOND",
        R.string.channel_bond,
        R.string.channel_bond_desc,
        NotificationManagerCompat.IMPORTANCE_DEFAULT
    ),

    LOCATION(
        "LOCATION",
        R.string.channel_location,
        R.string.channel_location_desc,
        NotificationManagerCompat.IMPORTANCE_MAX
    ),

    TASK("TASK",
        R.string.channel_task,
        R.string.channel_task_desc,
        NotificationManagerCompat.IMPORTANCE_DEFAULT
    );

    fun build(context: Context) = NotificationChannel(id, context.getString(title), importance).apply {
        description = this.description
    }

}