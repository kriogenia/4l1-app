package dev.sotoestevez.allforone.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.model.PrivateViewModel
import dev.sotoestevez.allforone.model.factories.ExtendedViewModelFactory
import dev.sotoestevez.allforone.util.extensions.errorToast
import dev.sotoestevez.allforone.util.extensions.logError

/**
 * Base Activity for all the Activities requiring user checking
 */
open class PrivateActivity : AppCompatActivity() {

	protected open val model: PrivateViewModel by viewModels { ExtendedViewModelFactory(this) }

	/**
	 * List of permitted roles in the Activity
	 */
	protected open val roles: Array<User.Role> = arrayOf()

	/**
	 * Override of the onCreate method
	 * Retrieves the user from the intent and checks the permissions
	 * If the user can't access this activity, the activity is killed and the user
	 * is returned to the authentication activity
	 * @param savedInstanceState bundle with a saved instance
	 */
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		checkUser()
		// Set observers
		model.error.observe(this, { handleError(it) })
	}

	/**
	 * Error observer
	 *
	 * @param error Registered error to handle
	 */
	private fun handleError(error: Throwable) {
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