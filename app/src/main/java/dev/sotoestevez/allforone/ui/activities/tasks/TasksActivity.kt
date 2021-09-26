package dev.sotoestevez.allforone.ui.activities.tasks

import androidx.activity.viewModels
import dev.sotoestevez.allforone.databinding.ActivityTasksBinding
import dev.sotoestevez.allforone.ui.view.PrivateActivity
import dev.sotoestevez.allforone.ui.viewmodel.ExtendedViewModelFactory
import dev.sotoestevez.allforone.vo.User
import java.util.*

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
        binding.fabTasks.setOnClickListener { model.updateTask() }
    }

}