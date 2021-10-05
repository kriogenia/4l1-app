package dev.sotoestevez.allforone.ui.components.recyclerview.feed

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import dev.sotoestevez.allforone.BR
import dev.sotoestevez.allforone.vo.feed.TaskMessage

abstract class TaskMessageView(private val data: TaskMessage): BaseObservable(), FeedView {

    /** Body of the message */
    val text: String = data.content

    /** Task description */
    val description: String? = data.task.description

    /** Sending time of the message */
    val time: String = data.time

    /** Bindable completion state of the task */
    @get:Bindable
    val done: Boolean
        get() = data.task.done

    /** Bindable state of the card */
    @get:Bindable var collapsed: Boolean = true
        private set

    /** Collapses expanded profile cards and expands collapse profile cards */
    fun onExpandButtonClick() {
        collapsed = !collapsed
        notifyPropertyChanged(BR.collapsed)
    }

}