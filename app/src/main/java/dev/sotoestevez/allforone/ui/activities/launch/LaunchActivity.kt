package dev.sotoestevez.allforone.ui.activities.launch

import android.app.Activity
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.databinding.ActivityLaunchBinding
import dev.sotoestevez.allforone.ui.viewmodel.ExtendedViewModelFactory
import dev.sotoestevez.allforone.ui.view.BaseExtendedActivity
import dev.sotoestevez.allforone.ui.activities.keeper.KeeperMainActivity
import dev.sotoestevez.allforone.ui.activities.patient.PatientMainActivity
import dev.sotoestevez.allforone.ui.activities.setup.SetUpActivity
import dev.sotoestevez.allforone.util.extensions.logDebug
import dev.sotoestevez.allforone.util.extensions.toast

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
		googleAuthHelper.setCallback({ onFailedAuthentication() }) { token -> model.handleSignInResult(token) }
		binding.btnSignIn.setOnClickListener {
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
		binding.btnSignIn.visibility = if (loading) GONE else VISIBLE
		binding.loadCircleLaunch.visibility = if (loading) VISIBLE else GONE
	}

	private fun onFailedAuthentication () {
		uiLoading(false)
		toast(getString(R.string.error_google_auth))
	}

}