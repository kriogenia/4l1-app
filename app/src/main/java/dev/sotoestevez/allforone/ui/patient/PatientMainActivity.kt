package dev.sotoestevez.allforone.ui.patient

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.databinding.ActivityPatientMainBinding
import dev.sotoestevez.allforone.model.ExtendedViewModelFactory
import dev.sotoestevez.allforone.model.patient.PatientMainViewModel
import dev.sotoestevez.allforone.ui.PrivateActivity
import java.util.*

/**
 * Main Activity of Patients
 */
class PatientMainActivity : PrivateActivity() {

	override val model: PatientMainViewModel by viewModels { ExtendedViewModelFactory(this) }

	private lateinit var binding: ActivityPatientMainBinding

	override val roles: EnumSet<User.Role> = EnumSet.of(User.Role.PATIENT)

	/**
	 * Override of the onCreate method
	 * @param savedInstanceState bundle with a saved instance
	 */
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
	}

	override fun bindLayout() {
		binding = ActivityPatientMainBinding.inflate(layoutInflater)
		binding.user = model.user.value
		setContentView(binding.root)
	}

	override fun attachListeners() {
		binding.profileCard.btnExpandCard.setOnClickListener {
			TransitionManager.beginDelayedTransition(binding.profileCard.cardUserProfile, AutoTransition())
			if (binding.profileCard.layExpandableSection.visibility == View.GONE) {
				binding.profileCard.layExpandableSection.visibility = View.VISIBLE
				binding.profileCard.btnExpandCard.rotation = 180f
			} else {
				binding.profileCard.layExpandableSection.visibility = View.GONE
				binding.profileCard.btnExpandCard.rotation = 0f
			}
		}
	}

}