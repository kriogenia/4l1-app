package dev.sotoestevez.allforone.ui.keeper

import android.os.Bundle
import dev.sotoestevez.allforone.databinding.ActivityKmainBinding
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.ui.PrivateActivity

/**
 *  Main activity of Keepers
 */
class KMainActivity : PrivateActivity() {

	private lateinit var binding: ActivityKmainBinding

	override var roles: Array<User.Role> = arrayOf<User.Role>(User.Role.KEEPER)

	/**
	 * Override of the onCreate method
	 * @param savedInstanceState bundle with a saved instance
	 */
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// Set the layout
		binding = ActivityKmainBinding.inflate(layoutInflater)
		setContentView(binding.root)
	}

	/**
	 * Override of the onStart method
	 */
	override fun onStart() {
		super.onStart()
		val text = "Logged in keeper: ${user.id}"
		binding.textView2.text = text
	}

}