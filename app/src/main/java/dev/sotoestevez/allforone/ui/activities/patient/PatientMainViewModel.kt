package dev.sotoestevez.allforone.ui.activities.patient

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dev.sotoestevez.allforone.ui.viewmodel.ExtendedViewModel
import dev.sotoestevez.allforone.ui.viewmodel.PrivateViewModel
import dev.sotoestevez.allforone.ui.viewmodel.WithProfileCard
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.repositories.GlobalRoomRepository
import dev.sotoestevez.allforone.repositories.NotificationRepository
import dev.sotoestevez.allforone.ui.viewmodel.WithNotifications
import dev.sotoestevez.allforone.util.dispatcher.DefaultDispatcherProvider
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import dev.sotoestevez.allforone.util.extensions.logDebug
import dev.sotoestevez.allforone.util.helpers.notifications.NotificationsManager
import dev.sotoestevez.allforone.util.helpers.notifications.ViewModelNotificationsHandlerImpl
import dev.sotoestevez.allforone.vo.Action
import dev.sotoestevez.allforone.vo.Notification
import kotlinx.coroutines.launch

/** View Model of the Main activity for Patients */
class PatientMainViewModel(
	savedStateHandle: SavedStateHandle,
	dispatchers: DispatcherProvider = DefaultDispatcherProvider,
	sessionRepository: SessionRepository,
	globalRoomRepository: GlobalRoomRepository,
	override val notificationRepository: NotificationRepository
): PrivateViewModel(savedStateHandle, dispatchers, sessionRepository), WithProfileCard, WithNotifications {

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

	/** WithProfileCard */
	override val profileCardExpandable: Boolean = true
	override val profileCardWithBanner: Boolean = true
	override val profileCardExpanded: MutableLiveData<Boolean> = MutableLiveData(false)

	@Suppress("unused") // Used in the factory with a class call
	constructor(builder: ExtendedViewModel.Builder) : this(
		builder.savedStateHandle,
		builder.dispatchers,
		builder.sessionRepository,
		builder.globalRoomRepository,
		builder.notificationRepository
	)

	init {
		viewModelScope.launch(dispatchers.io() + coroutineExceptionHandler) { notificationManager.load() }
		globalRoomRepository.join(user.value!!)
	}

	override fun setDestiny(destiny: Class<out Activity>) { mDestiny.value = destiny }

	override fun runNotificationRequest(request: suspend (String) -> Unit) {
		viewModelScope.launch(dispatchers.io() + coroutineExceptionHandler) { request(authHeader()) }
	}

}