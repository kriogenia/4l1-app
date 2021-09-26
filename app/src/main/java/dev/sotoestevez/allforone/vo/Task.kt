package dev.sotoestevez.allforone.vo

import android.content.res.Resources
import androidx.core.os.ConfigurationCompat
import com.google.gson.annotations.SerializedName
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

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

    /** Formatted local data time of the message */
    val time: String
        get() = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()).
            format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))

}