package dev.sotoestevez.allforone.model.patient

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dev.sotoestevez.allforone.model.ExtendedViewModel
import dev.sotoestevez.allforone.model.ExtendedViewModelFactory
import dev.sotoestevez.allforone.model.PrivateViewModel
import dev.sotoestevez.allforone.model.interfaces.WithProfileCard
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.repositories.SocketRepository
import dev.sotoestevez.allforone.util.dispatcher.DefaultDispatcherProvider
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider

/** View Model of the Main activity for Patients */
class PatientMainViewModel(
	savedStateHandle: SavedStateHandle,
	dispatchers: DispatcherProvider = DefaultDispatcherProvider,
	sessionRepository: SessionRepository = SessionRepository(),
	socketRepository: SocketRepository = SocketRepository()
): PrivateViewModel(savedStateHandle, dispatchers, sessionRepository), WithProfileCard {

	/** WithProfileCard */
	override val profileCardExpandable: Boolean = true
	override val profileCardWithBanner: Boolean = true
	override val profileCardExpanded: MutableLiveData<Boolean> = MutableLiveData(false)


	constructor(builder: ExtendedViewModel.Builder) : this(
		builder.savedStateHandle,
		builder.dispatchers,
		builder.sessionRepository,
		builder.socketRepository
	)

	init {
		socketRepository.connect(user.value?.id!!)
	}


}