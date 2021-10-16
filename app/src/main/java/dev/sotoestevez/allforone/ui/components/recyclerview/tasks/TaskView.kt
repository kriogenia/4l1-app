package dev.sotoestevez.allforone.ui.components.recyclerview.tasks

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import dev.sotoestevez.allforone.BR
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.ui.components.recyclerview.BindedItemView
import dev.sotoestevez.allforone.ui.components.recyclerview.tasks.listeners.TaskListener
import dev.sotoestevez.allforone.util.extensions.logDebug
import dev.sotoestevez.allforone.vo.Task
import dev.sotoestevez.allforone.vo.User

/**
 * ViewHolder for task cards
 *
 * @property data           Data of the task to display
 * @property listener       Button's action listener
 */
open class TaskView(
    var data: Task,
    val listener: TaskListener
) : BaseObservable(), BindedItemView {

    override val id: String = data.id

    override val layoutId: Int = R.layout.content_task

    override val viewType: Int = 0

    /** Bindable completion state of the task */
    @get:Bindable
    val done: Boolean
        get() = data.done

    /** Bindable last update time of the task */
    @get:Bindable
    val lastUpdate: Long
        get() = data.lastUpdate

    /** Bindable state of the card */
    @get:Bindable
    open var collapsed: Boolean = data.done
        protected set

    /** Changes the state of the task */
    fun swapState() {
        data.swapState()
        notifyPropertyChanged(BR.done)
        notifyPropertyChanged(BR.lastUpdate)
    }

    /** Invokes the done state update logic */
    fun onDoneButtonClick() = listener.onChangeDone(this)

    /** Collapses expanded profile cards and expands collapse profile cards */
    fun onExpandButtonClick() {
        collapsed = !collapsed
        notifyPropertyChanged(BR.collapsed)
    }

    /** Invokes the task deletion logic */
    fun onRemoveButtonClick() = listener.onDelete(this)

}