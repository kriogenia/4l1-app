package dev.sotoestevez.allforone.ui

import android.content.Intent
import android.os.Bundle
import android.security.keystore.UserNotAuthenticatedException
import androidx.appcompat.app.AppCompatActivity
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.entities.User
import dev.sotoestevez.allforone.util.logError
import kotlinx.android.synthetic.main.activity_pmain.*

/**
 * Base Activity for all the Activities requiring user checking
 */
open class PrivateActivity : AppCompatActivity() {

	/**
	 * Information of the current user.
	 *
	 * **WARNING** Don't access to user properties before the onStart phase as it could
	 * be null during the onCreate even with the check
	 */
	protected lateinit var user: User

	/**
	 * List of permitted roles in the Activity
	 */
	protected open var roles: Array<User.Role> = arrayOf<User.Role>()

	/**
	 * Override of the onCreate method
	 * Retrieves the user from the intent and checks the permissions
	 * If the user can't access this activity, the activity is killed and the user
	 * is returned to the authentication activity
	 * @param savedInstanceState bundle with a saved instance
	 */
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// Set the user
		try {
			user = loadUser()
		} catch (e: UserNotAuthenticatedException) {
			// if the user is not valid, return to the launch activity and kill this
			logError(e.message!!, e)
			startActivity(Intent(this, LaunchActivity::class.java))
			finish()
		}
	}

	/**
	 * Retrieves the user from the intent and checks if it's valid and has valid permissions
	 *
	 * @return the user if it's valid
	 */
	@Throws(UserNotAuthenticatedException::class)
	fun loadUser(): User {
		// Retrieve user
		val fromParcelable = intent.getParcelableExtra<User>(User::class.simpleName)
		// User is not logged in or has the wrong role, return to LaunchActivity
		if (fromParcelable != null) {
			if (roles.contains(fromParcelable.role)) {
				return fromParcelable
			}
			throw UserNotAuthenticatedException(getString(R.string.error_invalid_permissions))
		}
		throw UserNotAuthenticatedException(getString(R.string.error_not_authenticated_user))
	}

}