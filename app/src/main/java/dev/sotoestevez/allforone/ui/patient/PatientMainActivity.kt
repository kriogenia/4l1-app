package dev.sotoestevez.allforone.ui.patient

import android.view.View
import androidx.activity.viewModels
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.databinding.ActivityPatientMainBinding
import dev.sotoestevez.allforone.model.ExtendedViewModelFactory
import dev.sotoestevez.allforone.model.patient.PatientMainViewModel
import dev.sotoestevez.allforone.ui.PrivateActivity
import dev.sotoestevez.allforone.ui.bonds.BondsActivity
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
		binding.user = model.user.value
		setContentView(binding.root)
	}

	override fun attachListeners() {
		super.attachListeners()
		binding.profileCard.btnExpandCard.setOnClickListener {
			TransitionManager.beginDelayedTransition(binding.layPatientMain, AutoTransition())
			if (binding.profileCard.layExpandableSection.visibility == View.GONE) {
				binding.profileCard.layExpandableSection.visibility = View.VISIBLE
				binding.profileCard.btnExpandCard.rotation = 180f
			} else {
				binding.profileCard.layExpandableSection.visibility = View.GONE
				binding.profileCard.btnExpandCard.rotation = 0f
			}
		}
		binding.btnBonds.setOnClickListener { launchActivity(BondsActivity::class.java) }
	}

	private fun launchActivity(next: Class<BondsActivity>) {
		startActivity(buildIntent(next))
	}

}