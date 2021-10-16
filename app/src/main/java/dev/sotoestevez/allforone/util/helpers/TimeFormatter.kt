package dev.sotoestevez.allforone.util.helpers

import android.content.res.Resources
import androidx.core.os.ConfigurationCompat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

/** Helper object to manage timestamp conversions to strings */
object TimeFormatter {

    private val locale = ConfigurationCompat.getLocales(Resources.getSystem().configuration)[0]
    private val zoneId = ZoneId.systemDefault()

    /**
     * Returns a String with the time following the local user format
     *
     * @param timestamp instant to transform
     * @return time of the instant
     */
    fun getTime(timestamp: Long): String = SimpleDateFormat("HH:mm", locale).format(Date(timestamp))

    /**
     * Returns a String with the date following the local user format
     *
     * @param timestamp instant to transform
     * @return date of the instant
     */
    fun getDate(timestamp: Long): String = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), zoneId)
        .format(DateTimeFormatter.ofPattern("eee, dd MMM"))

    /**
     * Returns a String with the date and time following the local user format
     *
     * @param timestamp instant to transform
     * @return date and time of the instant
     */
    fun getDateTime(timestamp: Long): String = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), zoneId).
        format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))

}