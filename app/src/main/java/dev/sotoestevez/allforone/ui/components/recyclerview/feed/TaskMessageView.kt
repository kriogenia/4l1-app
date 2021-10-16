package dev.sotoestevez.allforone.ui.components.recyclerview.feed

import androidx.databinding.Bindable
import dev.sotoestevez.allforone.BR
import dev.sotoestevez.allforone.ui.components.recyclerview.tasks.TaskView
import dev.sotoestevez.allforone.ui.components.recyclerview.tasks.listeners.TaskListener
import dev.sotoestevez.allforone.vo.feed.TaskMessage

/**
 * Base class for Task Messages view
 *
 * @property data   Nested TaskMessage
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

    fun update(message: TaskMessage) {
        this.message = message
        data = message.task
        notifyPropertyChanged(BR.done)
    }

    fun onLongPress() = true.also { onRemoveButtonClick() }

}