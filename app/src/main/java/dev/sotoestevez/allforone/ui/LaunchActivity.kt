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
import dev.sotoestevez.allforone.entities.SessionManager
import dev.sotoestevez.allforone.entities.User
import dev.sotoestevez.allforone.ui.blank.SetUpActivity
import dev.sotoestevez.allforone.ui.keeper.KMainActivity
import dev.sotoestevez.allforone.ui.patient.PMainActivity
import dev.sotoestevez.allforone.util.*
import kotlinx.android.synthetic.main.activity_launch.*

/**
 * Launching activity of the project. It is in charge of managing the login of the user in the app.
 * Once a valid session is registered, the activity sends the user to the [MainActivity]
 */
class LaunchActivity : AppCompatActivity() {

	/**
	 * Launcher to the Sing-In intent sender for result. It manages the result of the intent and
	 * moves the user to the [MainActivity] if the logging is a success. Otherwise, it  manages the
	 * error resulted.
	 */
	private val gsiLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()
	) { result ->
		if (result.resultCode == Activity.RESULT_OK) {
			try {
				val credential = Identity.getSignInClient(this)
					.getSignInCredentialFromIntent(result.data)
				if (credential.googleIdToken != null) {
					handleSignInResult(credential.googleIdToken!!)
				}
			} catch (e: ApiException) {
				logError("Error retrieving user data from intent", e)
				//TODO add error managements - Toaster?
			}
		} else {
			logWarning("Google Sign-In intent failed and returned ${result.resultCode}")
			toast(getString(R.string.error_google_auth))
		}
	}

	/**
	 * Override of the onCreate method
	 * @param savedInstanceState bundle with a saved instance
	 */
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_launch)
		// Action for the sign-in button
		sign_in_button.setOnClickListener { invokeSignInAPI() }
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
	 * Launches an intent to call the Google Sign In API to perform the authentication
	 */
	private fun invokeSignInAPI() {
		// Build the sign-in request
		val request = GetSignInIntentRequest.builder()
			.setServerClientId(getString(R.string.server_client_id))
			.build()
		// Launches and manages the sign-in intent
		Identity.getSignInClient(this)
			.getSignInIntent(request)
			.addOnSuccessListener { signInResult ->
				logDebug("Launching Sign-In intent")
				gsiLauncher.launch(IntentSenderRequest.Builder(signInResult).build())
			}
			.addOnFailureListener { e ->
				logError("Google Sign-in failed", e)
				toast(getString(R.string.error_google_auth))
			}
	}

	/**
	 * Handles the retrieved token in the sign in request.
	 * Sends the Google token to the API to retrieve the User and the session tokens
	 * @param googleIdToken obtained in the authentication with Google
	 */
	private fun handleSignInResult(googleIdToken: String) {
		logInfo("Google-SignIn-Authentication: $googleIdToken")
		// Get authentication service
		val service = ApiFactory(this).getAuthService()
		// And perform the request to sign in
		val request = ApiRequest(this, suspend { service.signIn(googleIdToken) })
		request.performRequest(
			{ result -> completeAuthentication(result) },
			{ cause -> errorToast(cause) }
		)
	}

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