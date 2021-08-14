package dev.sotoestevez.allforone.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dev.sotoestevez.allforone.databinding.ActivityLaunchBinding
import dev.sotoestevez.allforone.entities.GoogleAuthHelper
import dev.sotoestevez.allforone.entities.User
import dev.sotoestevez.allforone.model.LaunchViewModel
import dev.sotoestevez.allforone.model.factories.ExtendedViewModelFactory
import dev.sotoestevez.allforone.ui.blank.SetUpActivity
import dev.sotoestevez.allforone.ui.keeper.KMainActivity
import dev.sotoestevez.allforone.ui.patient.PMainActivity
import dev.sotoestevez.allforone.util.*

/**
 * Launching activity of the project. It is in charge of managing the authentication of the user in the app.
 * Once a valid session is registered, the activity sends the user to the adequate main  activity
 * ([KMainActivity] or [PMainActivity]) or to the [SetUpActivity] if it's a new user.
 */
class LaunchActivity : AppCompatActivity() {

	private lateinit var binding: ActivityLaunchBinding
	private val viewModel: LaunchViewModel by viewModels { ExtendedViewModelFactory(this) }

	// Module with the logic to perform Google authentications
	private val googleAuthHelper = GoogleAuthHelper(this)

	/**
	 * Override of the onCreate method
	 * @param savedInstanceState bundle with a saved instance
	 */
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// Set the layout
		binding = ActivityLaunchBinding.inflate(layoutInflater)
		setContentView(binding.root)
		// Observe the destiny, state and error
		viewModel.error.observe(this, { handleError(it) })
		viewModel.destiny.observe(this, { nextActivity(it) })
		// Action for the sign-in button
		googleAuthHelper.setCallback { token -> viewModel.handleSignInResult(token) }
		binding.signInButton.setOnClickListener {
			uiLoading(true)
			googleAuthHelper.invokeSignInAPI()
		}
	}

	/**
	 * Override of the onStart method
	 */
	override fun onStart() {
		super.onStart()
		logDebug("Application started")
		// TODO silent sign in
	}

	/**
	 * Starts the next activity once the authentication is completed.
	 * Observer of the authentication process
	 *
	 * @param next Class of the activity to start
	 */
	private fun nextActivity(next: Class<out Activity>) {
		// Build the intent with the user and launch the activity
		val intent = Intent(this, next)
		intent.putExtra(User::class.simpleName, viewModel.user)
		startActivity(intent)
		// Delete the activity so it's not accessed going back
		finish()
	}

	/**
	 * Error observer
	 *
	 * @param error Registered error to handle
	 */
	private fun handleError(error: Throwable) {
		uiLoading(false)
		errorToast(error)
	}

	/**
	 * Changes the screen components to display based on the loading state
	 *
	 * @param loading	true if the authentication operation is being performed, false otherwise
	 */
	private fun uiLoading(loading: Boolean) {
		binding.signInButton.visibility = if (loading) GONE else VISIBLE
		binding.loadingBar.visibility = if (loading) VISIBLE else GONE
	}

}