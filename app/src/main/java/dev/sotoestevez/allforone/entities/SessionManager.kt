package dev.sotoestevez.allforone.entities

import androidx.lifecycle.SavedStateHandle
import dev.sotoestevez.allforone.util.extensions.logDebug
import java.time.Instant

/**
 * Entity to manage the session logic. It allows to open a new session, close it
 * and to query information about it
 *
 * @property state Saved State accessor
 */
class SessionManager(private val state: SavedStateHandle) {

	private val kLoggedIn = "logged_in"
	private val kAuthToken = "auth"
	private val kRefreshToken = "refresh"
	private val kExpiration = "expiration"

	/**
	 * Creates a new session. Storing all the session details in the state
	 *
	 * @param auth Authentication token of the session
	 * @param refresh Refresh token to get new authentication tokens
	 * @param expiration Expiration time of the current authentication token
	 */
	fun openSession(auth: String, refresh: String, expiration: Int) {
		state[kLoggedIn] = true;
		state[kAuthToken] = auth;
		state[kRefreshToken] = refresh;
		state[kExpiration] = expiration;
		logDebug("New session stored Auth[$auth]. Expires at $expiration")
	}

	/**
	 * Closes the current session deleting both tokens and the expiration time
	 */
	fun closeSession() {
		state.remove<Boolean>(kLoggedIn)
		state.remove<String>(kAuthToken)
		state.remove<String>(kRefreshToken)
		state.remove<Int>(kExpiration)
		logDebug("Current session closed")
	}

	/**
	 * Asserts if the user is currently logged in
	 *
	 * @return true if the session is open, false otherwise
	 */
	fun isLoggedIn(): Boolean {
		return state.get(kLoggedIn) ?: false
	}

	/**
	 * Retrieves the auth token from the Session
	 *
	 * @return auth token if it's still valid
	 */
	fun getToken(): String? {
		val expiration: Int = state.get(kExpiration) ?: 0
		val currentTime = Instant.now().epochSecond
		logDebug("Comparing times. Expiration[$expiration]. Current[$currentTime]")
		if (expiration > currentTime) {// TODO add margin to request a new one
			return state.get(kAuthToken)
		}
		logDebug("Authentication token expired. A new one should be retrieved.")
		return null
	}

}