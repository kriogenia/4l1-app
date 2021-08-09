package dev.sotoestevez.allforone.ui.keeper

import android.os.Bundle
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.entities.User
import dev.sotoestevez.allforone.ui.PrivateActivity
import kotlinx.android.synthetic.main.activity_pmain.*

/**
 *  Main activity of Keepers
 */
class KMainActivity : PrivateActivity() {

	override var roles: Array<User.Role> = arrayOf<User.Role>(User.Role.KEEPER)

	/**
	 * Override of the onCreate method
	 * @param savedInstanceState bundle with a saved instance
	 */
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_kmain)
	}

	/**
	 * Override of the onStart method
	 */
	override fun onStart() {
		super.onStart()
		val text = "Logged in keeper: ${user.id}"
		textView.text = text
	}

}