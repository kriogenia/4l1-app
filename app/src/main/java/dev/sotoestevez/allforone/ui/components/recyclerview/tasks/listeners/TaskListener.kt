package dev.sotoestevez.allforone.ui.components.recyclerview.tasks.listeners

import dev.sotoestevez.allforone.ui.components.recyclerview.tasks.TaskView

/** Interface to specify TaskView buttons logic */
interface TaskListener {

    /** Action to perform when using the done button */
    fun onChangeDone(view: TaskView)

    /** Action to perform when using the delete button */
    fun onDelete(view: TaskView)

}