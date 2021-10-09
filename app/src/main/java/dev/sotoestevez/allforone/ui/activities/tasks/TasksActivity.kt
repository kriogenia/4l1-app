package dev.sotoestevez.allforone.ui.activities.tasks

import android.system.Os.accept
import androidx.activity.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.databinding.ActivityTasksBinding
import dev.sotoestevez.allforone.ui.activities.tasks.fragments.CreateTaskDialog
import dev.sotoestevez.allforone.ui.components.recyclerview.tasks.TaskView
import dev.sotoestevez.allforone.ui.view.PrivateActivity
import dev.sotoestevez.allforone.ui.viewmodel.ExtendedViewModelFactory
import dev.sotoestevez.allforone.util.extensions.logDebug
import dev.sotoestevez.allforone.vo.Task
import dev.sotoestevez.allforone.vo.User
import java.util.*
import kotlin.math.log

/** Activity managing the list of tasks of the user */
class TasksActivity : PrivateActivity() {

    override val model: TasksViewModel by viewModels { ExtendedViewModelFactory(this) }

    private lateinit var binding: ActivityTasksBinding

    override val roles: EnumSet<User.Role> = EnumSet.of(User.Role.PATIENT)

    override fun bindLayout() {
        binding = ActivityTasksBinding.inflate(layoutInflater)
        binding.model = model
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }

    override fun attachListeners() {
        super.attachListeners()
        binding.fabTasks.setOnClickListener { openCreateTaskDialog() }
    }

    override fun attachObservers() {
        super.attachObservers()
        model.changedTask.observe(this) { openChangeTaskDialog(it) }
    }

    private fun openCreateTaskDialog() {
        CreateTaskDialog { title, desc -> model.createTask(title, desc) }
            .apply { submitter = model.user.value!!.displayName }
            .show(supportFragmentManager, CreateTaskDialog.TAG)
    }

    private fun openChangeTaskDialog(task: TaskView) {
        val title = if (task.done) R.string.revert_task_title else R.string.complete_task_title
        val message = if (task.done) R.string.revert_task_msg else R.string.complete_task_msg
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(title))
            .setMessage(String.format(getString(message), task.data.title))
            .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                logDebug("Canceled the change of Task[${task.data.id}]")
            }
            .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                model.setTaskDone(task)
            }
            .show()
    }

}