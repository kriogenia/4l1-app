package dev.sotoestevez.allforone.entities

import android.content.SharedPreferences
import dev.sotoestevez.allforone.util.logDebug
import java.time.Instant

/**
 * Entity to manage the session logic. It allows to open a new session, close it
 * and to query information about it
 *
 * @property sharedPrefs Shared Preferences accessor
 */
class SessionManager(private val sharedPrefs: SharedPreferences) {

	private val kLoggedIn = "logged_in"
	private val kAuthToken = "auth"
	private val kRefreshToken = "refresh"
	private val kExpiration = "expiration"

	/**
	 * Creates a new session. Storing all the session details in the preferences
	 *
	 * @param auth authentication token of the session
	 * @param refresh refresh token to get new authentication tokens
	 * @param expiration expiration time of the current authentication token
	 */
	fun openSession(auth: String, refresh: String, expiration: Int) {
		sharedPrefs.edit()
			.putBoolean(kLoggedIn, true)
			.putString(kAuthToken, auth)
			.putString(kRefreshToken, refresh)
			.putInt(kExpiration, expiration)
			.apply()
		logDebug("New session stored Auth[$auth]. Expires at $expiration")
	}

	/**
	 * Closes the current session deleting both tokens and the expiration time
	 */
	fun closeSession() {
		sharedPrefs.edit()
			.remove(kLoggedIn)
			.remove(kAuthToken)
			.remove(kRefreshToken)
			.remove(kExpiration)
			.apply()
		logDebug("Current session closed")
	}

	/**
	 * Asserts if the user is currently logged in
	 *
	 * @return true if the session is open, false otherwise
	 */
	fun isLoggedIn(): Boolean {
		return sharedPrefs.getBoolean(kLoggedIn, false)
	}

	/**
	 * Retrieves the auth token from the Session
	 *
	 * @return auth token if it's still valid
	 */
	fun getToken(): String? {
		val expiration = sharedPrefs.getInt(kExpiration, 0)
		val currentTime = Instant.now().epochSecond
		logDebug("Comparing times. Expiration[$expiration]. Current[$currentTime]")
		if (expiration > currentTime) {// TODO add margin to request a new one
			return sharedPrefs.getString(kAuthToken, null)
		}
		logDebug("Authentication token expired. A new one should be retrieved.")
		return null
	}

}