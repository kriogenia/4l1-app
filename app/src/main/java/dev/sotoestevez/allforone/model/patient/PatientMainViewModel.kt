package dev.sotoestevez.allforone.model.patient

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
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
		globalRoomRepository.connect(user.value?.id!!)
	}


}