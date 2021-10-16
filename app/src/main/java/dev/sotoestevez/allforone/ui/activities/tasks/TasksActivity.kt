package dev.sotoestevez.allforone.ui.activities.tasks

import androidx.activity.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.databinding.ActivityTasksBinding
import dev.sotoestevez.allforone.ui.activities.tasks.fragments.CreateTaskDialog
import dev.sotoestevez.allforone.ui.components.exchange.dialog.DialogConfirmationRequest
import dev.sotoestevez.allforone.ui.view.PrivateActivity
import dev.sotoestevez.allforone.ui.viewmodel.ExtendedViewModelFactory
import dev.sotoestevez.allforone.util.extensions.logDebug
import dev.sotoestevez.allforone.util.extensions.openActionConfirmationDialog
import dev.sotoestevez.allforone.vo.User
import java.util.*

/** Activity managing the list of tasks of the user */
class TasksActivity : PrivateActivity() {

    override val model: TasksViewModel by viewModels { ExtendedViewModelFactory(this) }

    private lateinit var binding: ActivityTasksBinding

    override val roles: EnumSet<User.Role> = EnumSet.of(User.Role.PATIENT, User.Role.KEEPER)

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
        model.error.observe(this) { handleError(it) }
        model.actionTaskToConfirm.observe(this) { openActionConfirmationDialog(it) }
    }

    private fun openCreateTaskDialog() {
        CreateTaskDialog { title, desc -> model.createTask(title, desc) }
            .apply { submitter = model.user.value!!.displayName }
            .show(supportFragmentManager, CreateTaskDialog.TAG)
    }

}