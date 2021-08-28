package dev.sotoestevez.allforone.ui

import android.content.Intent
import android.os.Bundle
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.ui.launch.LaunchActivity
import dev.sotoestevez.allforone.util.extensions.errorToast
import dev.sotoestevez.allforone.util.extensions.logError
import java.util.*

/**
 * Base Activity for all the Activities requiring user checking
 */
abstract class PrivateActivity : MyActivity() {

	/** List of permitted roles in the Activity */
	protected open val roles: EnumSet<User.Role> = EnumSet.noneOf(User.Role::class.java)

	@Suppress("KDocMissingDocumentation")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		checkUser()
	}

	override fun bindLayout() {}

	override fun attachListeners() {}

	override fun attachObservers() {
		model.error.observe(this, { handleError(it) })
	}

	override fun handleError(error: Throwable) {
		errorToast(error)
	}

	/**
	 * Retrieves the user from the intent and checks if it's valid and has valid permissions
	 *
	 * @return the user if it's valid
	 */
	private fun checkUser() {
		val user = model.user.value
		if (user == null || !roles.contains(user.role)) {
			logError(getString(R.string.error_invalid_permissions))
			startActivity(Intent(this, LaunchActivity::class.java))
			finish()
		}
	}

}