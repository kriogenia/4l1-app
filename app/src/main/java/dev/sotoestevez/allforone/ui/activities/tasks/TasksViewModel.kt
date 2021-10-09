package dev.sotoestevez.allforone.ui.activities.tasks

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.repositories.TaskRepository
import dev.sotoestevez.allforone.ui.activities.feed.FeedViewModel
import dev.sotoestevez.allforone.ui.components.recyclerview.tasks.TaskView
import dev.sotoestevez.allforone.ui.viewmodel.ExtendedViewModel
import dev.sotoestevez.allforone.ui.viewmodel.PrivateViewModel
import dev.sotoestevez.allforone.util.dispatcher.DefaultDispatcherProvider
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import dev.sotoestevez.allforone.util.extensions.invoke
import dev.sotoestevez.allforone.util.extensions.logDebug
import dev.sotoestevez.allforone.vo.Task
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** ViewModel of the TasksActivity */
class TasksViewModel(
    savedStateHandle: SavedStateHandle,
    dispatchers: DispatcherProvider = DefaultDispatcherProvider,
    sessionRepository: SessionRepository,
    private val taskRepository: TaskRepository
): PrivateViewModel(savedStateHandle, dispatchers, sessionRepository) {

    /** LiveData holding the list of pending tasks */
    val taskList: LiveData<List<TaskView>>
        get() = mTaskList
    private val mList: MutableList<TaskView> = mutableListOf()
    private val mTaskList: MutableLiveData<List<TaskView>> = MutableLiveData(mList)

    val changedTask: LiveData<TaskView>
        get() = mChangedTask
    private val mChangedTask: MutableLiveData<TaskView> = MutableLiveData()


    @Suppress("unused") // Used in the factory with a class call
    constructor(builder: ExtendedViewModel.Builder): this(
        builder.savedStateHandle,
        builder.dispatchers,
        builder.sessionRepository,
        builder.taskRepository
    )

    init {
        retrieveTasks()
    }

    /**
     * Creates a new task sending it to the server and displaying it in the list
     *
     * @param title Title of the task
     * @param description   Description of the task
     */
    fun createTask(title: String, description: String) {
        logDebug("Requested creation of new task: $title")
        viewModelScope.launch(dispatchers.io()) {
            val task = taskRepository.save(Task(title = title, description = description, submitter = user.value!!), authHeader())
            withContext(dispatchers.main()) {
                mList.add(wrapTask(task)).also { mTaskList.apply { postValue(value) } }
            }
            logDebug("New task created with id = [${task.id}] and title ${task.title}")
        }
    }

    private fun wrapTask(task: Task) = TaskView(task) { mChangedTask.value = it }

    private fun retrieveTasks() {
        if (loading.value!!) return
        loading.value = true
        viewModelScope.launch(dispatchers.io()) {
            val tasks = taskRepository.getTasks(authHeader())//.sortedBy { it.timestamp }
            withContext(dispatchers.main()) {
                mList.addAll(0, tasks.map { wrapTask(it) }).also { mTaskList.invoke() }
                loading.value = false
                Log.d(TasksViewModel::class.simpleName, "Retrieved list of tasks")
            }
        }
    }

    /**
     * Changes the task state
     *
     * @param task  Task to update
     */
    fun setTaskDone(task: TaskView) {
        task.swapState()
        viewModelScope.launch(dispatchers.io()) { taskRepository.updateDone(task.data, authHeader()) }
        logDebug("Changed the state of Task[${task.data.id}] to ${task.done}")
    }

}