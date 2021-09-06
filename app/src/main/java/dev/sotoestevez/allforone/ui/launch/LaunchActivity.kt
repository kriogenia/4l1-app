package dev.sotoestevez.allforone.ui.launch

import android.app.Activity
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import dev.sotoestevez.allforone.databinding.ActivityLaunchBinding
import dev.sotoestevez.allforone.entities.GoogleAuthHelper
import dev.sotoestevez.allforone.model.ExtendedViewModelFactory
import dev.sotoestevez.allforone.model.launch.LaunchViewModel
import dev.sotoestevez.allforone.ui.BaseExtendedActivity
import dev.sotoestevez.allforone.ui.keeper.KeeperMainActivity
import dev.sotoestevez.allforone.ui.patient.PatientMainActivity
import dev.sotoestevez.allforone.ui.setup.SetUpActivity
import dev.sotoestevez.allforone.util.extensions.logDebug

/**
 * Launching activity of the project. It is in charge of managing the authentication of the user in the app.
 * Once a valid session is registered, the activity sends the user to the adequate main  activity
 * ([KeeperMainActivity] or [PatientMainActivity]) or to the [SetUpActivity] if it's a new user.
 */
class LaunchActivity : BaseExtendedActivity() {

	override val model: LaunchViewModel by viewModels { ExtendedViewModelFactory(this) }

	private lateinit var binding: ActivityLaunchBinding

	// Module with the logic to perform Google authentications
	private val googleAuthHelper = GoogleAuthHelper(this)

	@Suppress("KDocMissingDocumentation")
	override fun onStart() {
		super.onStart()
		logDebug("Application started")
		// TODO silent sign in
	}

	override fun bindLayout() {
		binding = ActivityLaunchBinding.inflate(layoutInflater)
		setContentView(binding.root)
	}

	override fun attachListeners() {
		googleAuthHelper.setCallback { token -> model.handleSignInResult(token) }
		binding.signInButton.setOnClickListener {
			model.loading.value = true
			googleAuthHelper.invokeSignInAPI()
		}
	}

	override fun attachObservers() {
		model.error.observe(this) { handleError(it) }
		model.loading.observe(this) { uiLoading(it) }
		model.destiny.observe(this) { nextActivity(it) }
	}

	override fun handleError(error: Throwable) {
		model.loading.value = false
		super.handleError(error)
	}

	private fun nextActivity(next: Class<out Activity>) {
		startActivity(buildIntent(next))
		finish()
	}

	private fun uiLoading(loading: Boolean) {
		binding.signInButton.visibility = if (loading) GONE else VISIBLE
		binding.loadCircleLaunch.visibility = if (loading) VISIBLE else GONE
	}

}