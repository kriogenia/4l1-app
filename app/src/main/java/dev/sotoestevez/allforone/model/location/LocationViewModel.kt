package dev.sotoestevez.allforone.model.location

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import dev.sotoestevez.allforone.api.schemas.LocationShare
import dev.sotoestevez.allforone.entities.SocketManager
import dev.sotoestevez.allforone.model.ExtendedViewModel
import dev.sotoestevez.allforone.model.PrivateViewModel
import dev.sotoestevez.allforone.repositories.LocationRepository
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import dev.sotoestevez.allforone.util.extensions.logDebug
import kotlinx.coroutines.launch
import org.json.JSONObject

/** ViewModel managing the logic behind the LocationActivity ***/
class LocationViewModel(
	savedStateHandle: SavedStateHandle,
	dispatchers: DispatcherProvider,
	sessionRepository: SessionRepository = SessionRepository(),
	private val locationRepository: LocationRepository = LocationRepository()
): PrivateViewModel(savedStateHandle, dispatchers, sessionRepository) {

	/** Last user location */
	val lastKnownLocation: LiveData<Location?>
		get() = mLastKnownLocation
	private var mLastKnownLocation: MutableLiveData<Location?> = MutableLiveData(null)

	//val defaultLocation = LatLng(30.0175046, 32.5778113)

	constructor(builder: ExtendedViewModel.Builder): this(
		builder.savedStateHandle,
		builder.dispatchers,
		builder.sessionRepository,
		builder.locationRepository
	)

	init { locationRepository.startSharing(user.value!!) }

	/**
	 * Updates the location of the user, sending it through the socket and calling the observers
	 *
	 * @param newLocation   updated location
	 */
	fun updateLocation(newLocation: Location) {
		logDebug("Location: [${newLocation.latitude}, ${newLocation.longitude}]")
		mLastKnownLocation.value = newLocation
		locationRepository.update(user.value!!, newLocation)
	}

}