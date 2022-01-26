package dev.sotoestevez.allforone.ui.activities.patient

import androidx.activity.viewModels
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.databinding.ActivityPatientMainBinding
import dev.sotoestevez.allforone.ui.viewmodel.ExtendedViewModelFactory
import dev.sotoestevez.allforone.ui.view.PrivateActivity
import dev.sotoestevez.allforone.ui.activities.bonds.BondsActivity
import dev.sotoestevez.allforone.ui.activities.feed.FeedActivity
import dev.sotoestevez.allforone.ui.activities.location.LocationActivity
import dev.sotoestevez.allforone.ui.activities.tasks.TasksActivity
import dev.sotoestevez.allforone.ui.components.fragments.notifications.NotificationsDialog
import dev.sotoestevez.allforone.ui.components.fragments.settings.SettingsDialog
import java.util.*

/**
 * Main Activity of Patients
 */
class PatientMainActivity : PrivateActivity() {

    override val model: PatientMainViewModel by viewModels { ExtendedViewModelFactory(this) }

    private lateinit var binding: ActivityPatientMainBinding

    override val roles: EnumSet<User.Role> = EnumSet.of(User.Role.PATIENT)

    override fun bindLayout() {
        binding = ActivityPatientMainBinding.inflate(layoutInflater)
        binding.model = model
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }

    override fun attachListeners() {
        super.attachListeners()
        binding.buttons.run {
            btnBonds.setOnClickListener { startActivity(buildIntent(BondsActivity::class.java)) }
            btnLocation.setOnClickListener { startActivity(buildIntent(LocationActivity::class.java)) }
            btnTasks.setOnClickListener { startActivity(buildIntent(TasksActivity::class.java)) }
            btnFeed.setOnClickListener {
                startActivity(buildIntent(FeedActivity::class.java).apply {
                    putExtra(FeedActivity.OWNER, model.user.value!!.displayName)
                })
            }
        }
        binding.btnNotifications.setOnClickListener { openNotificationsDialog() }
        binding.btnSettings.setOnClickListener { openSettingsDialog() }
    }

    override fun attachObservers() {
        super.attachObservers()
        model.destiny.observe(this) { startActivity(buildIntent(it)) }
    }

    private fun openNotificationsDialog() {
        NotificationsDialog(model.notificationManager)
            .show(supportFragmentManager, NotificationsDialog.TAG)
    }

    private fun openSettingsDialog() {
        SettingsDialog(model.settingsHandler)
            .show(supportFragmentManager, SettingsDialog.TAG)
    }

}