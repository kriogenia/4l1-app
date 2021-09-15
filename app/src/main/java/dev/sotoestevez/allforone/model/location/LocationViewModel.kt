package dev.sotoestevez.allforone.model.location

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import dev.sotoestevez.allforone.api.requests.LocationShare
import dev.sotoestevez.allforone.entities.SocketManager
import dev.sotoestevez.allforone.model.ExtendedViewModel
import dev.sotoestevez.allforone.model.PrivateViewModel
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import dev.sotoestevez.allforone.util.extensions.logDebug
import org.json.JSONObject

class LocationViewModel(
	savedStateHandle: SavedStateHandle,
	dispatchers: DispatcherProvider,
	sessionRepository: SessionRepository = SessionRepository()
): PrivateViewModel(savedStateHandle, dispatchers, sessionRepository) {

	/** Last user location */
	val lastKnownLocation: LiveData<Location?>
		get() = mLastKnownLocation
	private var mLastKnownLocation: MutableLiveData<Location?> = MutableLiveData(null)

	//val defaultLocation = LatLng(30.0175046, 32.5778113)

	constructor(builder: ExtendedViewModel.Builder): this(
		builder.savedStateHandle,
		builder.dispatchers,
		builder.sessionRepository
	)

	init {
		SocketManager.socket.emit("location:share", JSONObject(Gson().toJson(LocationShare(user.value?.id!!, user.value?.displayName!!))))
	}

	fun updateLocation(newLocation: Location) {
		logDebug("Location: [${newLocation.latitude}, ${newLocation.longitude}]")
		mLastKnownLocation.value = newLocation
	}

}