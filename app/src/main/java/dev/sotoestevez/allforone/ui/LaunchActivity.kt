package dev.sotoestevez.allforone.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.common.api.ApiException
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.api.ApiFactory
import dev.sotoestevez.allforone.util.logDebug
import dev.sotoestevez.allforone.util.logError
import dev.sotoestevez.allforone.util.logInfo
import dev.sotoestevez.allforone.util.logWarning
import kotlinx.android.synthetic.main.activity_launch.*
import kotlinx.coroutines.launch

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
				handleSignInResult(credential)
			} catch (e: ApiException) {
				logError("Error retrieving user data from intent", e)
				//TODO add error managements - Toaster?
			}
		} else {
			logWarning("Google Sign-In intent failed and returned ${result.resultCode}")
			//TODO add error managements - Toaster?
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_launch)
		// Action for the sign-in button
		sign_in_button.setOnClickListener { invokeSignInAPI() }
	}

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
			}
	}

	private fun handleSignInResult(credential: SignInCredential) {
		logInfo("User logged in: ${credential.id}")
		val factory = ApiFactory(this)
		val service = factory.getAuthService()
		lifecycleScope.launch {
			val retrieved = service.sendCredentials()
			logDebug("Retrieved HttpCall: $retrieved")
		}.invokeOnCompletion {
			startActivity(Intent(this, MainActivity::class.java))
		}
	}

}