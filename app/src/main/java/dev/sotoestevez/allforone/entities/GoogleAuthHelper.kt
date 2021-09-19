package dev.sotoestevez.allforone.entities

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.ui.launch.LaunchActivity
import dev.sotoestevez.allforone.util.extensions.logDebug
import dev.sotoestevez.allforone.util.extensions.logError
import dev.sotoestevez.allforone.util.extensions.logWarning
import dev.sotoestevez.allforone.util.extensions.toast
import java.lang.IllegalStateException

/**
 * Helper class to manage all the logic related to the Google Authentication
 *
 * @property activity invoking the authentication
 */
class GoogleAuthHelper (private val activity: LaunchActivity) {

	private lateinit var authenticationLauncher: ActivityResultLauncher<IntentSenderRequest>

	/**
	 * Launches an intent to call the Google Sign In API to perform the authentication
	 */
	fun invokeSignInAPI() {
		if (!this::authenticationLauncher.isInitialized) throw IllegalStateException("Authentication callback not set")
		// Build the sign-in request
		val request = GetSignInIntentRequest.builder()
			.setServerClientId(activity.getString(R.string.server_client_id))
			.build()
		// Launches and manages the sign-in intent
		Identity.getSignInClient(activity)
			.getSignInIntent(request)
			.addOnSuccessListener { signInResult ->
				activity.logDebug("Launching Sign-In intent")
				authenticationLauncher.launch(IntentSenderRequest.Builder(signInResult).build())
			}
			.addOnFailureListener { e ->
				activity.logError("Google Sign-in failed", e)
				activity.toast(activity.getString(R.string.error_google_auth))
			}
	}

	/**
	 * Creates the launcher to send the activity for the SignInResult
	 * @param processToken callback to process the retrieved GoogleIdToken
	 */
	fun setCallback(
		processFailure: () -> Unit,
		processToken: (String) -> Unit
	) {
		authenticationLauncher = activity.registerForActivityResult(
			ActivityResultContracts.StartIntentSenderForResult()
		) { result ->
			if (result.resultCode == Activity.RESULT_OK) {
				try {
					val credential = Identity.getSignInClient(activity)
						.getSignInCredentialFromIntent(result.data)
					if (credential.googleIdToken != null)
						processToken(credential.googleIdToken!!)
					else
						activity.toast(activity.getString(R.string.error_invalid_google_account))
				} catch (e: ApiException) {
					activity.logError("Error retrieving user data from intent", e)
					processFailure()
				}
			} else {
				logWarning("Google Sign-In intent failed and returned ${result.resultCode}")
				processFailure()
			}
		}
	}

}