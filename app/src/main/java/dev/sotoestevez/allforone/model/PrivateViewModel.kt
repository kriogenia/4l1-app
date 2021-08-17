package dev.sotoestevez.allforone.model

import androidx.lifecycle.*
import dev.sotoestevez.allforone.entities.SessionManager
import dev.sotoestevez.allforone.repositories.UserRepository
import dev.sotoestevez.allforone.util.dispatcher.DefaultDispatcherProvider
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import dev.sotoestevez.allforone.util.extensions.logDebug
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel of the the Activities using session (derived from PrivateActivity)
 *
 * @property dispatchers [DispatcherProvider] to inject the dispatchers
 * @constructor
 * To create the ViewModel
 *
 * @param savedStateHandle [SavedStateHandle] object to store session data
 */
open class PrivateViewModel(
	savedStateHandle: SavedStateHandle,
	private val dispatchers: DispatcherProvider = DefaultDispatcherProvider
): ViewModel() {

	private val sessionManager: SessionManager = SessionManager(savedStateHandle)

	/**
	 * Live data holding the error to handle in the Activity
	 */
	val error: LiveData<Throwable>
		get() = _error
	private var _error = MutableLiveData<Throwable>()

	/**
	 * Retrieves the stored token if it's still valid. If it's not, refresh the current token
	 * with the refresh token and obtain and store a new one
	 *
	 * @return Authentication token
	 */
	private fun token(): String {
		val storedToken = sessionManager.getToken()
		// If the stored token is still valid, use it
		if (storedToken != null)
			return storedToken
		// In case it's not, get a new one
		viewModelScope.launch(dispatchers.io() + coroutineExceptionHandler) {
			val result = UserRepository.refresh(sessionManager.getRefreshToken())
			// Store all the session info
			val ( auth, refresh, expiration ) = result
			logDebug("Authentication refreshed. Auth[$auth]")
			sessionManager.setSession( auth, refresh, expiration)
			// Returns the retrieved token
			return@launch auth
		}
	}

	/**
	 * Base coroutine exception handler
	 */
	protected open val coroutineExceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
		_error.postValue(throwable)
	}

}