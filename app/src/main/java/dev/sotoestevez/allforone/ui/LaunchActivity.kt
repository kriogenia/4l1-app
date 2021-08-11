package dev.sotoestevez.allforone.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.entities.GoogleAuthHelper
import dev.sotoestevez.allforone.entities.User
import dev.sotoestevez.allforone.model.LaunchViewModel
import dev.sotoestevez.allforone.model.factories.LaunchViewModelFactory
import dev.sotoestevez.allforone.ui.blank.SetUpActivity
import dev.sotoestevez.allforone.ui.keeper.KMainActivity
import dev.sotoestevez.allforone.ui.patient.PMainActivity
import dev.sotoestevez.allforone.util.*
import kotlinx.android.synthetic.main.activity_launch.*

/**
 * Launching activity of the project. It is in charge of managing the login of the user in the app.
 * Once a valid session is registered, the activity sends the user to the he adequate main
 * activity ([KMainActivity] or [PMainActivity]) or to the [SetUpActivity] if it's a new user.
 */
class LaunchActivity : AppCompatActivity() {

	private val viewModel: LaunchViewModel by viewModels { LaunchViewModelFactory(this) }

	private val googleAuthHelper = GoogleAuthHelper(this)

	/**
	 * Override of the onCreate method
	 * @param savedInstanceState bundle with a saved instance
	 */
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_launch)
		// Observe the destiny
		viewModel.destiny.observe(this, Observer { it -> nextActivity(it) })
		// Action for the sign-in button
		googleAuthHelper.setCallback { token -> viewModel.handleSignInResult(this, token) }
		sign_in_button.setOnClickListener { googleAuthHelper.invokeSignInAPI() }
	}

	/**
	 * Override of the onStart method
	 */
	override fun onStart() {
		super.onStart()
		logDebug("Application started")
		// TODO silent sign in
	}

	private fun nextActivity(next: Class<out Activity>) {
		// Build the intent with the user and launch the activity
		val intent = Intent(this, next)
		intent.putExtra(User::class.simpleName, viewModel.user)
		startActivity(intent)
		// Delete the activity so it's not accessed going back
		finish()
	}

}