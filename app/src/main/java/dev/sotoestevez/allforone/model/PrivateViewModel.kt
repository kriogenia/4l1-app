package dev.sotoestevez.allforone.model

import androidx.lifecycle.*
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.entities.SessionManager
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import dev.sotoestevez.allforone.util.extensions.logDebug
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import org.jetbrains.annotations.TestOnly

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
	protected val dispatchers: DispatcherProvider
): ViewModel(), ExtendedViewModel {

	companion object {
		private val USER = User::class.java.simpleName
	}

	override val sessionManager: SessionManager = SessionManager(savedStateHandle)

	private var sessionRepo = SessionRepository()

	/** Mutable implementation of the user live data exposed **/
	protected var mUser: MutableLiveData<User> = MutableLiveData<User>(savedStateHandle[USER])
	override val user: LiveData<User>
		get() = mUser

	/** Mutable implementation of the error live data exposed **/
	private var mError = MutableLiveData<Throwable>()
	override val error: LiveData<Throwable>
		get() = mError

	override val loading: MutableLiveData<Boolean> = MutableLiveData(false)

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
			val newSession = sessionRepo.refreshSession(session)
			logDebug("Authentication refreshed. Auth[${newSession.auth}]")
			sessionManager.setSession(newSession)
			// Returns the retrieved token
			return@async "Bearer ${newSession.auth}"
		}
		return tokenJob.await()
	}

	/** Base coroutine exception handler */
	protected open val coroutineExceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
		mError.postValue(throwable)
	}

	@TestOnly
	internal fun injectUser(user: User) {
		mUser.value = user
	}

	@TestOnly
	internal fun injectSessionRepo(sessionRepository: SessionRepository) {
		sessionRepo = sessionRepository;
	}

}