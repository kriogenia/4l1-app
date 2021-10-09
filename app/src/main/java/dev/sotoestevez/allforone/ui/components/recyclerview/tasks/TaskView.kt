package dev.sotoestevez.allforone.ui.components.recyclerview.tasks

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import dev.sotoestevez.allforone.BR
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.ui.components.recyclerview.BindedItemView
import dev.sotoestevez.allforone.vo.Task

/**
 * ViewHolder for task cards
 *
 * @property data Data of the task to display
 * @property onDoneButtonClick Callback to change task state
 */
class TaskView(
    val data: Task,
    val onDoneButtonClick: (TaskView) -> Unit
) : BaseObservable(), BindedItemView {

    override val layoutId: Int = R.layout.content_task

    override val viewType: Int = 0

    /** Bindable completion state of the task */
    @get:Bindable val done: Boolean
        get() = data.done

    /** Bindable state of the card */
    @get:Bindable var collapsed: Boolean = data.done
        private set

    /** Changes the state of the task */
    fun swapState() {
        data.done = !data.done
        notifyPropertyChanged(BR.done)
    }

    /** Collapses expanded profile cards and expands collapse profile cards */
    fun onExpandButtonClick() {
        collapsed = !collapsed
        notifyPropertyChanged(BR.collapsed)
    }

}