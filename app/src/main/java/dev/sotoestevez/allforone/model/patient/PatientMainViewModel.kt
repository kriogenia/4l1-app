package dev.sotoestevez.allforone.model.patient

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.google.gson.Gson
import dev.sotoestevez.allforone.api.requests.GlobalSubscribe
import dev.sotoestevez.allforone.entities.SocketManager
import dev.sotoestevez.allforone.model.PrivateViewModel
import dev.sotoestevez.allforone.model.interfaces.WithProfileCard
import dev.sotoestevez.allforone.model.interfaces.WithSocket
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.util.dispatcher.DefaultDispatcherProvider
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import io.socket.client.Socket

/** View Model of the Main activity for Patients */
class PatientMainViewModel(
	savedStateHandle: SavedStateHandle,
	dispatchers: DispatcherProvider = DefaultDispatcherProvider,
	sessionRepository: SessionRepository = SessionRepository()
): PrivateViewModel(savedStateHandle, dispatchers, sessionRepository), WithProfileCard, WithSocket {

	/** With Socket **/
	override val socket: Socket = SocketManager.socket

	/** WithProfileCard */
	override val profileCardExpandable: Boolean = true
	override val profileCardWithBanner: Boolean = true
	override val profileCardExpanded: MutableLiveData<Boolean> = MutableLiveData(false)

	init {
		socket.on("connect") {
			socket.emit("global:subscribe", toJson(GlobalSubscribe(user.value?.id!!, user.value!!.id!!)))
		}
		SocketManager.start()
	}

}