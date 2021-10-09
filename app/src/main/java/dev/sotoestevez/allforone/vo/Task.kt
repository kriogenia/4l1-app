package dev.sotoestevez.allforone.vo

import com.google.gson.annotations.SerializedName
import dev.sotoestevez.allforone.util.helpers.TimeFormatter
import java.time.Instant

/**
 * Class representing the tasks to Patients
 *
 * @property id             Unique identifier of the task
 * @property title          Title of the task
 * @property submitter      Keeper that submitted the tasks if it was not created by the Patient
 * @property done           Status of the task
 * @property description    Extra information of the task
 * @property timestamp      Creation timestamp of the task
 */
data class Task(
    @SerializedName("_id") val id: String = "",
    val title: String,
    val submitter: User,
    val description: String? = null,
    var done: Boolean = false,
    val timestamp: Long = Instant.now().toEpochMilli()
) {

    /** Timestamp of the last task update */
    var lastUpdate: Long? = null

    /** Formatted local data time of the message */
    val dateTime: String
        get() = TimeFormatter.getDateTime(timestamp)

    /** Formatted local data time of the message */
    val updateTime: String?
        get() = lastUpdate?.let { TimeFormatter.getDateTime(it) }

    /** Swaps the done state of the task*/
    fun swapState() {
        done = !done
        this.lastUpdate = Instant.now().toEpochMilli()
    }

}