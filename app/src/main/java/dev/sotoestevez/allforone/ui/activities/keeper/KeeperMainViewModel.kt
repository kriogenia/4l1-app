package dev.sotoestevez.allforone.ui.activities.keeper

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.repositories.GlobalRoomRepository
import dev.sotoestevez.allforone.repositories.NotificationRepository
import dev.sotoestevez.allforone.repositories.UserRepository
import dev.sotoestevez.allforone.ui.activities.launch.LaunchActivity
import dev.sotoestevez.allforone.ui.viewmodel.*
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import dev.sotoestevez.allforone.util.extensions.logDebug
import dev.sotoestevez.allforone.util.helpers.notifications.NotificationsManager
import dev.sotoestevez.allforone.util.helpers.notifications.ViewModelNotificationsHandler
import dev.sotoestevez.allforone.util.helpers.notifications.ViewModelNotificationsHandlerImpl
import dev.sotoestevez.allforone.util.helpers.settings.ViewModelSettingsHandler
import dev.sotoestevez.allforone.util.helpers.settings.ViewModelSettingsHandlerImpl
import dev.sotoestevez.allforone.vo.Action
import dev.sotoestevez.allforone.vo.Notification
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** ViewModel to handle the logic of the keeper's main activity */
class KeeperMainViewModel(
    savedStateHandle: SavedStateHandle,
    dispatchers: DispatcherProvider,
    sessionRepository: SessionRepository,
    override val userRepository: UserRepository,
    private val globalRoomRepository: GlobalRoomRepository,
    override val notificationRepository: NotificationRepository
) : PrivateViewModel(savedStateHandle, dispatchers, sessionRepository), WithProfileCard, WithNotifications, WithSettings {

    /** LiveData holding the info about the patient cared by this user */
    val cared: LiveData<User>
        get() = mCared
    private val mCared = MutableLiveData<User>(null)

    /** Live data holding the class of the next activity to launch from the LaunchActivity **/
    val destiny: LiveData<Class<out Activity>>
        get() = mDestiny
    private var mDestiny = MutableLiveData<Class<out Activity>>()

    // WithProfileCard
    override val profileCardReversed: MutableLiveData<Boolean> = MutableLiveData(false)

    /** Entity in charge of managing the notifications */
    val notificationManager: NotificationsManager by lazy {
        NotificationsManager(ViewModelNotificationsHandlerImpl(this))
    }

    /** Entity in charge of managing the settings */
    val settingsHandler: ViewModelSettingsHandler by lazy {
        ViewModelSettingsHandlerImpl(this)
    }

    @Suppress("unused") // Used in the factory with a class call
    constructor(builder: ExtendedViewModel.Builder) : this(
        builder.savedStateHandle,
        builder.dispatchers,
        builder.sessionRepository,
        builder.userRepository,
        builder.globalRoomRepository,
        builder.notificationRepository
    )

    init {
        loading.value = true
        // Load cared user
        logDebug("Requesting info of cared user")
        viewModelScope.launch(dispatchers.io() + coroutineExceptionHandler) {
            userRepository.getCared(user.value!!, authHeader())?.let { setCared(it) }
            logDebug("Yey")
            loading.postValue(false)
            logDebug(loading.value.toString())
        }
        viewModelScope.launch(dispatchers.io() + coroutineExceptionHandler) { notificationManager.load() }
    }


    /**
     * Sends the scanned code to the server to bond with the user that provided the code
     *
     * @param code  Scanned code to perform the bond operation
     */
    fun bond(code: String) {
        logDebug("[${user.value?.id}] scanned QR Code: ${code.substring(0, 6)}...")
        loading.value = true
        viewModelScope.launch(dispatchers.io() + coroutineExceptionHandler) {
            viewModelScope.launch(dispatchers.io() + coroutineExceptionHandler) {
                Log.d(KeeperMainViewModel::class.simpleName, "Sending bonding code")
                userRepository.sendBondingCode(code, authHeader())
            }.join()    // Wait until the send request finishes to know the outcome, if no error was thrown it was a success
            val response = userRepository.getCared(user.value!!, authHeader())
                ?: throw NullPointerException("Unable to forge the bond, please try again")
            Log.d(KeeperMainViewModel::class.simpleName, "Bond accepted")
            setCared(response)
        }
    }

    private suspend fun setCared(cared: User) {
        logDebug("Retrieved cared user ${cared.displayName}")
        withContext(dispatchers.main()) { mCared.value = cared }
        // Start socket connection
        notificationManager.subscribe()
        globalRoomRepository.join(user.value!!)
    }

    override fun setDestiny(destiny: Class<out Activity>) {
        mDestiny.value = destiny
    }

    override fun runRequest(request: suspend (String) -> Unit) {
        viewModelScope.launch(dispatchers.io() + coroutineExceptionHandler) { request(authHeader()) }
    }

    override fun toLaunch() {
        sessionManager.closeSession()
        mUser.postValue(null)
    }

}