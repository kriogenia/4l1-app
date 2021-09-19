package dev.sotoestevez.allforone.entities

import androidx.lifecycle.SavedStateHandle
import dev.sotoestevez.allforone.data.Session
import dev.sotoestevez.allforone.util.extensions.logDebug
import java.time.Instant

/**
 * Entity to manage the session logic. It allows to open a new session, close it
 * and to query information about it
 *
 * @property state Saved State accessor
 */
class SessionManager(private val state: SavedStateHandle) {

	companion object {
		private const val LOGGED_IN = "logged_in"
		private const val MARGIN = 60
		private val SESSION = Session::class.java.simpleName
	}

	/**
	 * Creates a new session. Storing all the session details in the state
	 *
	 * @param session Session data with the tokens and expiration time
	 */
	fun setSession(session: Session) {
		state[LOGGED_IN] = true
		state[SESSION] = session
		logDebug("New session stored Auth[${session.auth}]. Expires at ${session.expiration}")
	}

	/**
	 * Closes the current session deleting both tokens and the expiration time
	 */
	fun closeSession() {
		state.remove<Boolean>(LOGGED_IN)
		state.remove<Session>(SESSION)
		logDebug("Current session closed")
	}

	/**
	 * Asserts if the user is currently logged in
	 *
	 * @return true if the session is open, false otherwise
	 */
	private fun isLoggedIn(): Boolean {
		return state.get(LOGGED_IN) ?: false
	}

	/**
	 * Retrieves the authentication token from the Session
	 *
	 * @return Authentication token if it's still valid
	 */
	fun getAuthToken(): String? {
		val session = getSession() ?: return null
		val currentTime = Instant.now().epochSecond
		logDebug("Comparing times. Expiration[${session.expiration}]. Current[$currentTime]")
		if (session.expiration > currentTime + MARGIN) {
			return session.auth
		}
		logDebug("Authentication token expired. A new one should be retrieved.")
		return null
	}

	/**
	 * Retrieves the current active session
	 *
	 * @return Session with the tokens and expiration time
	 */
	fun getSession(): Session? = state.get(SESSION)

}