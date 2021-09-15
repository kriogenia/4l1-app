package dev.sotoestevez.allforone.model.patient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.model.ExtendedViewModel
import dev.sotoestevez.allforone.model.PrivateViewModel
import dev.sotoestevez.allforone.model.interfaces.WithProfileCard
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.repositories.GlobalRoomRepository
import dev.sotoestevez.allforone.util.dispatcher.DefaultDispatcherProvider
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider

/** View Model of the Main activity for Patients */
class PatientMainViewModel(
	savedStateHandle: SavedStateHandle,
	dispatchers: DispatcherProvider = DefaultDispatcherProvider,
	sessionRepository: SessionRepository = SessionRepository(),
	globalRoomRepository: GlobalRoomRepository = GlobalRoomRepository()
): PrivateViewModel(savedStateHandle, dispatchers, sessionRepository), WithProfileCard {

	/** LiveData holding the identifier of the message to show in the warning panel */
	val warning: LiveData<Int>
		get() = mWarning
	private val mWarning = MutableLiveData(-1)

	/** User sharing its location */
	var sharing: String = ""

	/** WithProfileCard */
	override val profileCardExpandable: Boolean = true
	override val profileCardWithBanner: Boolean = true
	override val profileCardExpanded: MutableLiveData<Boolean> = MutableLiveData(false)


	constructor(builder: ExtendedViewModel.Builder) : this(
		builder.savedStateHandle,
		builder.dispatchers,
		builder.sessionRepository,
		builder.globalRoomRepository
	)

	init {
		globalRoomRepository.onSharingLocation() {
			sharing = it
			mWarning.postValue(R.string.warn_searching)
		}
		globalRoomRepository.connect(user.value?.id!!)
	}


}