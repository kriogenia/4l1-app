package dev.sotoestevez.allforone.model.keeper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.model.PrivateViewModel
import dev.sotoestevez.allforone.model.interfaces.WithProfileCard
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.repositories.UserRepository
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import dev.sotoestevez.allforone.util.extensions.logDebug
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.NullPointerException

/** ViewModel to handle the logic of the keeper's main activity */
class KeeperMainViewModel(
    savedStateHandle: SavedStateHandle,
    dispatchers: DispatcherProvider,
    sessionRepository: SessionRepository = SessionRepository(),
    private val userRepository: UserRepository = UserRepository()
) : PrivateViewModel(savedStateHandle, dispatchers, sessionRepository), WithProfileCard {

    /** LiveData holding the info about the patient cared by this user */
    val cared: LiveData<User>
        get() = mCared
    private val mCared = MutableLiveData<User>(null)

    // withProfileCards interface
    override val profileCardExpandable: Boolean = true
    override val profileCardWithBanner: Boolean = true
    override val profileCardExpanded: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
    	loading.value = true
        // Load cared user
        viewModelScope.launch(dispatchers.io() + coroutineExceptionHandler) {
            val response = userRepository.getCared(authHeader())
            withContext(dispatchers.main()) {
                loading.value = false
                mCared.value = response     // updates retrieved cared user
            }
        }
    }

    /**
     * Sends the scanned code to the server to bond with the user that provided the code
     *
     * @param code  Scanned code to perform the bond operation
     */
    fun bond(code: String) {
        logDebug("[${user.value?.id}] scanned QR Code: ${code.substring(0,6)}...")
        loading.value = true
        viewModelScope.launch(dispatchers.io() + coroutineExceptionHandler) {
            viewModelScope.launch(dispatchers.io() + coroutineExceptionHandler) {
                userRepository.sendBondingCode(code, authHeader())
            }.join()    // Wait until the send request finishes to know the outcome, if no error was thrown it was a success
            val response = userRepository.getCared(authHeader()) ?: throw NullPointerException("Unable to forge the bond, please try again")
            withContext(dispatchers.main()) {
                loading.value = false
                mCared.value = response     // updates the cared user
            }
        }
    }

}