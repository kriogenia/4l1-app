package dev.sotoestevez.allforone.ui.components.recyclerview.tasks

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import dev.sotoestevez.allforone.BR
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.ui.components.recyclerview.BindedItemView
import dev.sotoestevez.allforone.vo.Task
import dev.sotoestevez.allforone.vo.User

/**
 * ViewHolder for task cards
 *
 * @property data           Data of the task to display
 * @property onChangeDone   Callback to change task state
 */
class TaskView(
    val data: Task,
    val onChangeDone: (TaskView) -> Unit
) : BaseObservable(), BindedItemView {

    override val layoutId: Int = R.layout.content_task

    override val viewType: Int = 0

    /** Bindable completion state of the task */
    @get:Bindable val done: Boolean
        get() = data.done

    /** Bindable last update time of the task */
    @get:Bindable val lastUpdate: Long
        get() = data.lastUpdate

    /** Bindable state of the card */
    @get:Bindable var collapsed: Boolean = data.done
        private set

    /** Changes the state of the task */
    fun swapState() {
        data.swapState()
        notifyPropertyChanged(BR.done)
        notifyPropertyChanged(BR.lastUpdate)
    }

    /** Invokes the done state change logic */
    fun onDoneButtonClick() = onChangeDone(this)

    /** Collapses expanded profile cards and expands collapse profile cards */
    fun onExpandButtonClick() {
        collapsed = !collapsed
        notifyPropertyChanged(BR.collapsed)
    }

}