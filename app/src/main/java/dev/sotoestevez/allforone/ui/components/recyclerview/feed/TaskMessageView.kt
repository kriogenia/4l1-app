package dev.sotoestevez.allforone.ui.components.recyclerview.feed

import androidx.databinding.Bindable
import dev.sotoestevez.allforone.BR
import dev.sotoestevez.allforone.ui.components.recyclerview.tasks.TaskView
import dev.sotoestevez.allforone.ui.components.recyclerview.tasks.listeners.TaskListener
import dev.sotoestevez.allforone.vo.feed.TaskMessage

/**
 * Base class for Task Messages view
 *
 * @property message   Nested TaskMessage
 * @property listener  Action listener
 */
sealed class TaskMessageView(
    var message: TaskMessage,
    listener: TaskListener
) : TaskView(message.task, listener), FeedView {

    /** Body of the message */
    val text: String = message.content

    /** Task description */
    val description: String? = data.description

    /** Sending time of the message */
    val time: String = message.time

    /** Bindable state of the card */
    @get:Bindable
    override var collapsed: Boolean = true

    /**
     * Updates the state of the task
     *
     * @param newDone   New state
     */
    fun updateState(newDone: Boolean) {
        this.message.task.done = newDone
        data.done = newDone
        notifyPropertyChanged(BR.done)
    }

    /** On long press action listener */
    fun onLongPress() = true.also { onRemoveButtonClick() }

}