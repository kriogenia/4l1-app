package dev.sotoestevez.allforone.ui.activities.patient

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dev.sotoestevez.allforone.repositories.GlobalRoomRepository
import dev.sotoestevez.allforone.repositories.NotificationRepository
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.repositories.UserRepository
import dev.sotoestevez.allforone.ui.viewmodel.*
import dev.sotoestevez.allforone.util.dispatcher.DefaultDispatcherProvider
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import dev.sotoestevez.allforone.util.helpers.notifications.NotificationsManager
import dev.sotoestevez.allforone.util.helpers.notifications.ViewModelNotificationsHandlerImpl
import dev.sotoestevez.allforone.util.helpers.settings.ViewModelSettingsHandler
import dev.sotoestevez.allforone.util.helpers.settings.ViewModelSettingsHandlerImpl
import dev.sotoestevez.allforone.vo.Notification
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

/** View Model of the Main activity for Patients */
class PatientMainViewModel(
    savedStateHandle: SavedStateHandle,
    dispatchers: DispatcherProvider = DefaultDispatcherProvider,
    sessionRepository: SessionRepository,
    globalRoomRepository: GlobalRoomRepository,
    override val userRepository: UserRepository,
    override val notificationRepository: NotificationRepository
) : PrivateViewModel(savedStateHandle, dispatchers, sessionRepository), WithProfileCard, WithNotifications, WithSettings {

    /** LiveData holding the identifier of the message to show in the warning panel */
    val notification: LiveData<Notification>
        get() = mNotification
    private val mNotification: MutableLiveData<Notification> = MutableLiveData(null)

    /** Live data holding the class of the next activity to launch from the LaunchActivity **/
    val destiny: LiveData<Class<out Activity>>
        get() = mDestiny
    private var mDestiny = MutableLiveData<Class<out Activity>>()

    /** Entity in charge of managing the notifications */
    val notificationManager: NotificationsManager by lazy {
        NotificationsManager(ViewModelNotificationsHandlerImpl(this))
    }

    /** Entity in charge of managing the settings */
    val settingsHandler: ViewModelSettingsHandler by lazy {
        ViewModelSettingsHandlerImpl(this, true)
    }

    /** WithProfileCard */
    override val profileCardReversed: MutableLiveData<Boolean> = MutableLiveData(false)

    @Suppress("unused") // Used in the factory with a class call
    constructor(builder: ExtendedViewModel.Builder) : this(
        builder.savedStateHandle,
        builder.dispatchers,
        builder.sessionRepository,
        builder.globalRoomRepository,
        builder.userRepository,
        builder.notificationRepository
    )

    init {
        viewModelScope.launch(dispatchers.io() + coroutineExceptionHandler) { notificationManager.load() }
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

    override fun removeBond(callback: () -> Unit) {
        throw IllegalStateException("Patients can't remove its bonds from here")
    }

}