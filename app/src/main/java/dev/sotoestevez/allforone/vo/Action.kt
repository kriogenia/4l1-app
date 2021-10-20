package dev.sotoestevez.allforone.vo

import android.app.Activity
import android.content.Context
import com.google.gson.annotations.SerializedName
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.ui.activities.bonds.BondsActivity
import dev.sotoestevez.allforone.ui.activities.location.LocationActivity
import dev.sotoestevez.allforone.ui.activities.tasks.TasksActivity
import java.util.*

/**
 * Enumeration of the possible notifiable actions
 *
 * @property destiny    Activity to navigate in case of pressing the notification
 */
enum class Action(private val template: Int, val destiny: Class<out Activity>? = null) {

    /** A new bond has been established */
    @SerializedName("bond_created") BOND_CREATED (
        R.string.user_has_bonded,
        BondsActivity::class.java
    ),

    /** User has created a new task */
    @SerializedName("task_created") TASK_CREATED (
        R.string.user_created_new_task,
        TasksActivity::class.java
    ),

    /** User has deleted a new task */
    @SerializedName("task_deleted") TASK_DELETED (
        R.string.user_deleted_task
    ),

    /** User has deleted a new task */
    @SerializedName("task_done") TASK_DONE (
        R.string.user_completed_task,
        TasksActivity::class.java
    ),

    /** User has deleted a new task */
    @SerializedName("task_undone") TASK_UNDONE (
        R.string.user_set_task_not_done,
        TasksActivity::class.java
    ),

    /** User started sharing its location */
    @SerializedName("location_sharing_start") LOCATION_SHARING_START (
        R.string.warn_sharing_location,
        LocationActivity::class.java
    ),

    /** User stopped sharing its location */
    @SerializedName("location_sharing_stop") LOCATION_SHARING_STOP (
        R.string.user_stopped_sharing
    );

    /** Socket event path of the notification */
    val path = "notify:${name.lowercase(Locale.getDefault())}"

    /**
     * Returns the string to print the action
     *
     * @param context   Context to retrieve the string template
     * @param args      Arguments to add to the template
     * @return          Printable string
     */
    fun print(context: Context, vararg args: String): String = String.format(context.getString(template), *args)

}