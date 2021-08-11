package dev.sotoestevez.allforone.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.api.ApiFactory
import dev.sotoestevez.allforone.api.data.SignInResponse
import dev.sotoestevez.allforone.api.ApiRequest
import dev.sotoestevez.allforone.entities.GoogleAuthHelper
import dev.sotoestevez.allforone.entities.SessionManager
import dev.sotoestevez.allforone.entities.User
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

	private val googleAuthHelper = GoogleAuthHelper(this)

	/**
	 * Override of the onCreate method
	 * @param savedInstanceState bundle with a saved instance
	 */
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_launch)
		// Action for the sign-in button
		googleAuthHelper.setCallback { token -> handleSignInResult(token) }
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

	// TODO move to ViewModel
	/**
	 * Handles the retrieved token in the sign in request.
	 * Sends the Google token to the API to retrieve the User and the session tokens
	 * @param googleIdToken obtained in the authentication with Google
	 */
	private fun handleSignInResult(googleIdToken: String) {
		logInfo("Google-SignIn-Authentication: $googleIdToken")
		// Get authentication service
		// TODO move to repo
		val service = ApiFactory(this).getAuthService()
		// And perform the request to sign in
		val request = ApiRequest(this, suspend { service.signIn(googleIdToken) })
		request.performRequest(
			{ result -> completeAuthentication(result) },
			{ cause -> errorToast(cause) }
		)
	}

	// TODO move to ViewModel
	/**
	 * Once all the authorization is validated, complete it saving the session tokens and
	 * opening the next activity
	 *
	 * @param authData authentication data with the tokens and user info
	 */
	private fun completeAuthentication(authData: SignInResponse) {
		logDebug("Authentication validated. User[${authData.user.id}]")
		val ( auth, refresh, expiration, user ) = authData
		SessionManager.openSession(this, auth, refresh, expiration)
		// Decide the activity to navigate based on the user role
		val destiny = when (user.role) {
			User.Role.KEEPER -> KMainActivity::class.java
			User.Role.PATIENT -> PMainActivity::class.java
			User.Role.BLANK -> SetUpActivity::class.java
		}
		// Build the intent with the user and launch the activity
		val intent = Intent(this, destiny)
		intent.putExtra(User::class.simpleName, user)
		startActivity(intent)
		// Delete the activity so it's not accessed going back
		finish()
	}

}