package dev.sotoestevez.allforone.data

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dev.sotoestevez.allforone.util.extensions.logDebug

/**
 * Object representing a marker in the map showing the location of an user
 *
 * @property id unique identifier of the user
 * @property displayName name of the user
 * @property position latitude and longitude of the user
 */
data class UserMarker(
	val id: String,
	val displayName: String,
	val position: LatLng
) {

	/**
	 * Creates the equivalent MarkerOptions object of Google to show the marker in the map
	 *
	 * @return Google marker
	 */
	fun build(): MarkerOptions =  MarkerOptions().title(displayName).position(position)

}