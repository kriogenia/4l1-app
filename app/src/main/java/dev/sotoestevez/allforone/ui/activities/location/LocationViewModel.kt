package dev.sotoestevez.allforone.ui.activities.location

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.Marker
import dev.sotoestevez.allforone.vo.UserMarker
import dev.sotoestevez.allforone.ui.viewmodel.ExtendedViewModel
import dev.sotoestevez.allforone.ui.viewmodel.PrivateViewModel
import dev.sotoestevez.allforone.repositories.LocationRepository
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.ui.activities.location.helpers.MarkerManager
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import dev.sotoestevez.allforone.util.extensions.logDebug
import kotlinx.coroutines.launch

/** ViewModel managing the logic behind the LocationActivity ***/
class LocationViewModel(
    savedStateHandle: SavedStateHandle,
    dispatchers: DispatcherProvider,
    sessionRepository: SessionRepository,
    private val locationRepository: LocationRepository
) : PrivateViewModel(savedStateHandle, dispatchers, sessionRepository) {

    /** Last user location */
    val lastKnownLocation: LiveData<Location?>
        get() = mLastKnownLocation
    private var mLastKnownLocation: MutableLiveData<Location?> = MutableLiveData(null)

    /** LiveData holding the data of a new marker to add */
    val newUserMarker: LiveData<UserMarker?>
        get() = mNewMarker
    private val mNewMarker: MutableLiveData<UserMarker?> = MutableLiveData(null)

    /** LiveData holding a marker that left the location room */
    val leavingUserMarker: LiveData<Marker?>
        get() = mLeavingUserMarker
    private val mLeavingUserMarker: MutableLiveData<Marker?> = MutableLiveData(null)

    private val markerManager by lazy { MarkerManager() }

    @Suppress("unused") // Used in the factory with a class call
    constructor(builder: ExtendedViewModel.Builder) : this(
        builder.savedStateHandle,
        builder.dispatchers,
        builder.sessionRepository,
        builder.locationRepository
    )

    init {
        locationRepository.join(user.value!!)
        locationRepository.onExternalUpdate { onExternalUpdate(it) }
        locationRepository.onUserLeaving { mLeavingUserMarker.postValue(markerManager.remove(it.id)) }
    }

    @Suppress("KDocMissingDocumentation")
    override fun onCleared() {
        locationRepository.leave(user.value!!)
        super.onCleared()
    }

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

    /**
     * Stores the marker in the marker manager
     *
     * @param marker    marker to store
     */
    fun storeMarker(marker: Marker): Unit = markerManager.add(marker)

    private fun onExternalUpdate(marker: UserMarker) {
        if (markerManager.exists(marker)) {
            viewModelScope.launch(dispatchers.main()) {
                markerManager.update(marker)
            }
        } else {
            mNewMarker.postValue(marker.also { markerManager.assignColor(it) })
        }
    }

}