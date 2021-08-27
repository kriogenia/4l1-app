package dev.sotoestevez.allforone.model

import androidx.lifecycle.*
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.entities.SessionManager
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.util.dispatcher.DefaultDispatcherProvider
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import dev.sotoestevez.allforone.util.extensions.logDebug
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import java.lang.IllegalStateException

/**
 * ViewModel of the the Activities using session (derived from PrivateActivity)
 *
 * @property dispatchers [DispatcherProvider] to inject the dispatchers
 * @constructor
 * To create the ViewModel
 *
 * @param savedStateHandle [SavedStateHandle] object to store session data
 */
abstract class PrivateViewModel(
	savedStateHandle: SavedStateHandle,
	protected val dispatchers: DispatcherProvider = DefaultDispatcherProvider
): ViewModel() {

	companion object {
		private val USER = User::class.java.simpleName
	}

	/**	Module to manage the tokens currently stored in memory */
	private val sessionManager: SessionManager = SessionManager(savedStateHandle)

	/** Live data holding the user information */
	val user: LiveData<User>
		get() = _user
	protected var _user = MutableLiveData<User>(savedStateHandle[USER])

	/** Live data holding the error to handle in the Activity */
	val error: LiveData<Throwable>
		get() = _error
	private var _error = MutableLiveData<Throwable>()

	/**
	 * Retrieves the stored token if it's still valid. If it's not, refresh the current token
	 * with the refresh token and obtain and store a new one
	 *
	 * @return Authentication token
	 */
	internal suspend fun authHeader(): String {
		val storedToken = sessionManager.getAuthToken()
		// If the stored token is still valid, use it
		if (storedToken != null)
			return "Bearer $storedToken"
		// In case it's not, get a new one
		val tokenJob = viewModelScope.async(dispatchers.io() + coroutineExceptionHandler) {
			val session = sessionManager.getSession() ?: throw IllegalStateException("Missing session data in private activity")
			val newSession = SessionRepository.refreshSession(session).session
			logDebug("Authentication refreshed. Auth[${newSession.auth}]")
			sessionManager.setSession(newSession)
			// Returns the retrieved token
			return@async "Bearer ${newSession.auth}"
		}
		return tokenJob.await()
	}

	/** Base coroutine exception handler */
	protected open val coroutineExceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
		_error.postValue(throwable)
	}

	/************ TDD oriented injections ******************/

	internal fun injectUser(user: User) {
		_user.value = user
	}

}