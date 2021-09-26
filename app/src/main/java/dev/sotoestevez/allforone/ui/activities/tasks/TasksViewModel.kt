package dev.sotoestevez.allforone.ui.activities.tasks

import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.ui.components.recyclerview.BindedItemView
import dev.sotoestevez.allforone.ui.components.recyclerview.tasks.TaskView
import dev.sotoestevez.allforone.ui.viewmodel.ExtendedViewModel
import dev.sotoestevez.allforone.ui.viewmodel.PrivateViewModel
import dev.sotoestevez.allforone.util.dispatcher.DefaultDispatcherProvider
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import dev.sotoestevez.allforone.vo.Task

/** ViewModel of the TasksActivity */
class TasksViewModel(
    savedStateHandle: SavedStateHandle,
    dispatchers: DispatcherProvider = DefaultDispatcherProvider,
    sessionRepository: SessionRepository
): PrivateViewModel(savedStateHandle, dispatchers, sessionRepository) {

    /** LiveData holding the list of pending tasks */
    val taskList: LiveData<List<TaskView>>
        get() = mTaskList
    private val mList: MutableList<TaskView> = mutableListOf()
    private val mTaskList: MutableLiveData<List<TaskView>> = MutableLiveData(mList)

    @Suppress("unused") // Used in the factory with a class call
    constructor(builder: ExtendedViewModel.Builder): this(
        builder.savedStateHandle,
        builder.dispatchers,
        builder.sessionRepository
    )

    init {
        mList.add(TaskView(Task("", "New", user.value!!)))
        mList.add(TaskView(Task("", "This one has a longer title", user.value!!,
        "This it the description of this task, the others don't have, what a losers", true)))
        mList.add(TaskView(Task("", "This one has a title that needs two rows", user.value!!,
        "Ok, this one also has a description, it seems")))
    }

    fun updateTask() {
        mList[0].data.done = true.also { mList[0].notifyPropertyChanged(BR.done) }
    }

}