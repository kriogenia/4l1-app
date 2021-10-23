package dev.sotoestevez.allforone.vo

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Object representing a marker in the map showing the location of an user
 *
 * @property id unique identifier of the user
 * @property displayName name of the user
 * @property position latitude and longitude of the user
 * @property color color of the marker
 */
data class UserMarker(
    val id: String,
    val displayName: String,
    val position: LatLng,
    var color: Int? = null
) {

    /**
     * Creates the equivalent MarkerOptions object of Google to show the marker in the map
     *
     * @return Google marker
     */
    fun build(): MarkerOptions = MarkerOptions().title(displayName).position(position)

}