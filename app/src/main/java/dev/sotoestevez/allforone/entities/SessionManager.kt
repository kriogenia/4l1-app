package dev.sotoestevez.allforone.entities

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import java.time.Instant

/**
 * Entity to manage the session logic. It allows to open a new session, close it
 * and to query information about it
 */
object SessionManager {

	private val TAG = this::class.simpleName
	private val LOGGED_IN = this.javaClass::class.qualifiedName + ".logged_in"
	private val AUTH_TOKEN = this.javaClass::class.qualifiedName + ".auth"
	private val REFRESH_TOKEN = this.javaClass::class.qualifiedName + ".refresh"
	private val EXPIRATION = this.javaClass::class.qualifiedName + ".expiration"

	/**
	 * Creates a new session. Storing all the session details in the preferences
	 *
	 * @param context of the request
	 * @param auth authentication token of the session
	 * @param refresh refresh token to get new authentication tokens
	 * @param expiration expiration time of the current authentication token
	 */
	fun openSession(context: Context, auth: String, refresh: String, expiration: Int) {
		val sharedPrefs = getSharedPrefs(context)
		sharedPrefs.edit()
			.putBoolean(LOGGED_IN, true)
			.putString(AUTH_TOKEN, auth)
			.putString(REFRESH_TOKEN, refresh)
			.putInt(EXPIRATION, expiration)
			.apply()
		Log.d(TAG, "New session stored Auth[$auth]. Expires at $expiration")
	}

	/**
	 * Closes the current session deleting both tokens and the expiration time
	 *
	 * @param context of the request
	 */
	fun closeSession(context: Context) {
		val sharedPrefs = getSharedPrefs(context)
		sharedPrefs.edit()
			.remove(LOGGED_IN)
			.remove(AUTH_TOKEN)
			.remove(REFRESH_TOKEN)
			.remove(EXPIRATION)
			.apply()
		Log.d(TAG, "Current session closed")
	}

	/**
	 * Asserts if the user is currently logged in
	 *
	 * @param context of the request
	 * @return true if the session is open, false otherwise
	 */
	fun isLoggedIn(context: Context): Boolean {
		return getSharedPrefs(context).getBoolean(LOGGED_IN, false)
	}

	/**
	 * Retrieves the auth token from the Session
	 *
	 * @param context of the request
	 * @return auth token if it's still valid
	 */
	fun getToken(context: Context): String? {
		val sharedPrefs = getSharedPrefs(context)
		val expiration = sharedPrefs.getInt(EXPIRATION, 0)
		val currentTime = Instant.now().epochSecond
		Log.d(TAG, "Comparing times. Expiration[$expiration]. Current[$currentTime]")
		if (expiration > currentTime) {// TODO add margin to request a new one
			return sharedPrefs.getString(AUTH_TOKEN, null)
		}
		Log.d(TAG, "Authentication token expired. Retrieving a new one.")
		return null
	}


	/**
	 * @param context of the request
	 * @return SharedPreferences object of the Session
	 */
	private fun getSharedPrefs(context: Context): SharedPreferences {
		return context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
	}

}