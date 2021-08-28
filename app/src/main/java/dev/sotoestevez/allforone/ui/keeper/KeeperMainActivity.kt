package dev.sotoestevez.allforone.ui.keeper

import android.os.Bundle
import androidx.activity.viewModels
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.databinding.ActivityKeeperMainBinding
import dev.sotoestevez.allforone.model.ExtendedViewModelFactory
import dev.sotoestevez.allforone.model.patient.PatientMainViewModel
import dev.sotoestevez.allforone.ui.PrivateActivity
import java.util.*

/**
 *  Main activity of Keepers
 */
class KeeperMainActivity : PrivateActivity() {

	// TODO replace
	override val model: PatientMainViewModel by viewModels { ExtendedViewModelFactory(this) }

	private lateinit var binding: ActivityKeeperMainBinding

	override val roles: EnumSet<User.Role> = EnumSet.of(User.Role.KEEPER)

	/**
	 * Override of the onCreate method
	 * @param savedInstanceState bundle with a saved instance
	 */
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// Set the layout
		binding = ActivityKeeperMainBinding.inflate(layoutInflater)
		setContentView(binding.root)
	}

	override fun bindLayout() {
		TODO("Not yet implemented")
	}

	/**
	 * Override of the onStart method
	 */
	override fun onStart() {
		super.onStart()
		val text = "Logged in keeper: ${model.user.value?.id}"
		binding.textView2.text = text
	}

}