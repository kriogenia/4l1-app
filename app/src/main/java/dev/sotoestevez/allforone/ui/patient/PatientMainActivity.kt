package dev.sotoestevez.allforone.ui.patient

import androidx.activity.viewModels
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.databinding.ActivityPatientMainBinding
import dev.sotoestevez.allforone.model.ExtendedViewModelFactory
import dev.sotoestevez.allforone.model.patient.PatientMainViewModel
import dev.sotoestevez.allforone.ui.PrivateActivity
import dev.sotoestevez.allforone.ui.bonds.BondsActivity
import dev.sotoestevez.allforone.ui.location.LocationActivity
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
		binding.btnBonds.setOnClickListener { startActivity(buildIntent(BondsActivity::class.java)) }
		binding.btnShareLocation.setOnClickListener { startActivity(buildIntent(LocationActivity::class.java)) }
	}

}