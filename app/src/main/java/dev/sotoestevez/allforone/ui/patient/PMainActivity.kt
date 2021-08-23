package dev.sotoestevez.allforone.ui.patient

import android.os.Bundle
import dev.sotoestevez.allforone.databinding.ActivityPmainBinding
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.ui.PrivateActivity

/**
 * Main Activity of Patients
 */
class PMainActivity : PrivateActivity() {

	private lateinit var binding: ActivityPmainBinding

	override var roles: Array<User.Role> = arrayOf<User.Role>(User.Role.PATIENT)

	/**
	 * Override of the onCreate method
	 * @param savedInstanceState bundle with a saved instance
	 */
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// Set the layout
		binding = ActivityPmainBinding.inflate(layoutInflater)
		setContentView(binding.root)
	}

	/**
	 * Override of the onStart method
	 */
	override fun onStart() {
		super.onStart()
		val text = "Logged in patient: ${model.user.value?.id}"
		binding.textView.text = text
	}
}