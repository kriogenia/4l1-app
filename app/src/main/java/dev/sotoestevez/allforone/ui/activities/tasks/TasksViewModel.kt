package dev.sotoestevez.allforone.ui.activities.tasks

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.repositories.TaskRepository
import dev.sotoestevez.allforone.ui.components.exchange.dialog.DeleteTaskConfirmation
import dev.sotoestevez.allforone.ui.components.exchange.dialog.DialogConfirmationRequest
import dev.sotoestevez.allforone.ui.components.exchange.dialog.SetTaskDoneConfirmation
import dev.sotoestevez.allforone.ui.components.recyclerview.tasks.TaskView
import dev.sotoestevez.allforone.ui.components.recyclerview.tasks.listeners.TaskListener
import dev.sotoestevez.allforone.ui.viewmodel.ExtendedViewModel
import dev.sotoestevez.allforone.ui.viewmodel.PrivateViewModel
import dev.sotoestevez.allforone.util.dispatcher.DefaultDispatcherProvider
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import dev.sotoestevez.allforone.util.extensions.invoke
import dev.sotoestevez.allforone.util.extensions.logDebug
import dev.sotoestevez.allforone.vo.Task
import dev.sotoestevez.allforone.vo.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException

/** ViewModel of the TasksActivity */
class TasksViewModel(
    savedStateHandle: SavedStateHandle,
    dispatchers: DispatcherProvider = DefaultDispatcherProvider,
    sessionRepository: SessionRepository,
    private val taskRepository: TaskRepository
) : PrivateViewModel(savedStateHandle, dispatchers, sessionRepository) {

    /** LiveData holding the list of pending tasks */
    val taskList: LiveData<List<TaskView>>
        get() = mTaskList
    private val mList: MutableList<TaskView> = mutableListOf()
    private val mTaskList: MutableLiveData<List<TaskView>> = MutableLiveData(mList)

    /** LiveData holding the last task changed */
    val actionTaskToConfirm: LiveData<DialogConfirmationRequest>
        get() = mActionTaskToConfirm
    private val mActionTaskToConfirm: MutableLiveData<DialogConfirmationRequest> = MutableLiveData()

    /** Mutable implementation of the error live data exposed **/
    private var mError = MutableLiveData<Throwable>()
    override val error: LiveData<Throwable>
        get() = mError

    @Suppress("unused") // Used in the factory with a class call
    constructor(builder: ExtendedViewModel.Builder) : this(
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
        if (title.isBlank()) {
            return
        }
        viewModelScope.launch(dispatchers.io()) {
            val task = taskRepository.save(Task(title = title, description = description, submitter = user.value!!), authHeader())
            withContext(dispatchers.main()) {
                mList.add(wrapTask(task)).also { mTaskList.apply { postValue(value) } }
            }
            logDebug("New task created with id = [${task.id}] and title ${task.title}")
        }
    }

    private fun wrapTask(task: Task) = TaskView(task, object : TaskListener {
        override fun onChangeDone(view: TaskView) {
            mActionTaskToConfirm.value = SetTaskDoneConfirmation(view) { setTaskDone(it) }
        }

        override fun onDelete(view: TaskView) {
            if (user.value!!.role != User.Role.PATIENT && view.data.submitter != user.value) {
                mError.value = IllegalAccessException("Only the task submitter or the patient can delete this task")
                return
            }
            mActionTaskToConfirm.value = DeleteTaskConfirmation(view) { deleteTask(it) }
        }

    })

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

    private fun setTaskDone(task: TaskView) {
        task.swapState()
        viewModelScope.launch(dispatchers.io()) { taskRepository.updateDone(task.data, authHeader()) }
        logDebug("Changed the state of Task[${task.data.id}] to ${task.done}")
    }

    private fun deleteTask(task: TaskView) {
        viewModelScope.launch(dispatchers.io()) { taskRepository.delete(task.data, authHeader()) }
        mList.remove(task).also { mTaskList.invoke() }
        logDebug("Deleted the Task[${task.data.id}]")
    }

}