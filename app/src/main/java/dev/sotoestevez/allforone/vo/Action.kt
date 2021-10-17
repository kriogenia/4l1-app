package dev.sotoestevez.allforone.vo

import android.content.Context
import com.google.gson.annotations.SerializedName
import dev.sotoestevez.allforone.R
import java.util.*

/** Enumeration of the possible notifiable actions */
enum class Action {

    /** User started sharing its location */
    @SerializedName("location_sharing_start") LOCATION_SHARING_START {
        override fun print(context: Context, vararg args: String): String {
            return String.format(context.getString(R.string.warn_sharing_location), *args)
        }
    },

    /** User stopped sharing its location */
    @SerializedName("location_sharing_stop") LOCATION_SHARING_STOP {
        override fun print(context: Context, vararg args: String): String {
            return String.format(context.getString(R.string.user_stopped_sharing), *args)
        }
    };

    /** Socket event path of the notification */
    val path = "notify:${name.lowercase(Locale.getDefault())}"

    /**
     * Returns the string to print the action
     *
     * @param context   Context to retrieve the string template
     * @param args      Arguments to add to the template
     * @return          Printable string
     */
    abstract fun print(context: Context, vararg args: String): String

}