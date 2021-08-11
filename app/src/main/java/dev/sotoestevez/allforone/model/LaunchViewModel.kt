package dev.sotoestevez.allforone.model

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.*
import dev.sotoestevez.allforone.api.ApiFactory
import dev.sotoestevez.allforone.api.ApiRequest
import dev.sotoestevez.allforone.api.data.SignInResponse
import dev.sotoestevez.allforone.entities.SessionManager
import dev.sotoestevez.allforone.entities.User
import dev.sotoestevez.allforone.ui.LaunchActivity
import dev.sotoestevez.allforone.ui.blank.SetUpActivity
import dev.sotoestevez.allforone.ui.keeper.KMainActivity
import dev.sotoestevez.allforone.ui.patient.PMainActivity
import dev.sotoestevez.allforone.util.errorToast
import dev.sotoestevez.allforone.util.logDebug

class LaunchViewModel(
	savedStateHandle: SavedStateHandle,
	sharedPreferences: SharedPreferences
): ViewModel() {

	val sessionManager = SessionManager(sharedPreferences)

	/**
	 * Live data holding the class of the next activity to launch from the LaunchActivity
	 */
	val destiny: LiveData<Class<out Activity>>
		get() = _destiny
	private var _destiny = MutableLiveData<Class<out Activity>>()

	public var user: User? = null

	/**
	 * Handles the retrieved token in the sign in request.
	 * Sends the Google token to the API to retrieve the User and the session tokens
	 * @param googleIdToken obtained in the authentication with Google
	 */
	public fun handleSignInResult(context: LaunchActivity, googleIdToken: String) {
		logDebug("Google-SignIn-Authentication: $googleIdToken")
		// Get authentication service
		// TODO move to repo
		val service = ApiFactory.getAuthService()
		// And perform the request to sign in
		val request = ApiRequest(this, suspend { service.signIn(googleIdToken) })
		request.performRequest(
			{ result -> completeAuthentication(context, result) },
			{ cause -> context.errorToast(cause) }
		)
	}

	/**
	 * Once all the authorization is validated, complete it saving the session tokens and
	 * opening the next activity
	 *
	 * @param authData authentication data with the tokens and user info
	 */
	private fun completeAuthentication(context: LaunchActivity, authData: SignInResponse) {
		user = authData.user
		logDebug("Authentication validated. User[${user!!.id}]")
		val ( auth, refresh, expiration, user ) = authData
		sessionManager.openSession( auth, refresh, expiration)
		// Decide the activity to navigate based on the user role
		_destiny.value = when (user.role) {
			User.Role.KEEPER -> KMainActivity::class.java
			User.Role.PATIENT -> PMainActivity::class.java
			User.Role.BLANK -> SetUpActivity::class.java
		}
	}



}