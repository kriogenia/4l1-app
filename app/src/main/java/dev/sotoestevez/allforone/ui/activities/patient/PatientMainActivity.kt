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
		binding.run {
			btnBonds.setOnClickListener { startActivity(buildIntent(BondsActivity::class.java)) }
			btnShareLocation.setOnClickListener { startActivity(buildIntent(LocationActivity::class.java)) }
			btnTask.setOnClickListener { startActivity(buildIntent(TasksActivity::class.java)) }
			btnFeed.setOnClickListener {
				startActivity(buildIntent(FeedActivity::class.java).apply {
					putExtra(FeedActivity.OWNER, model!!.user.value!!.displayName)
				})
			}
		}
	}

}