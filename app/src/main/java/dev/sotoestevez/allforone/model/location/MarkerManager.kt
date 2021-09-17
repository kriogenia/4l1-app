package dev.sotoestevez.allforone.model.location

import com.google.android.gms.maps.model.Marker
import dev.sotoestevez.allforone.data.UserMarker

/** Class to manage the list of markers in the location map */
class MarkerManager {

	private val current: MutableMap<String, Marker> = mutableMapOf()

	/**
	 * Stores the marker in the map to keep track of it
	 *
	 * @param marker    new map marker
	 */
	fun add(marker: Marker) {
		// set color?
		val id: String = (marker.tag as String?) ?: throw IllegalStateException("A marker is lacking a tag with the id")
		current[id] = marker
	}

	/**
	 * Checks if the marker of the specified user exists
	 *
	 * @param userMarker    marker data
	 */
	fun exists(userMarker: UserMarker): Boolean = current.contains(userMarker.id)

	/**
	 * Updates the position of the map marker
	 *
	 * @param userMarker    new marker data
	 */
	fun update(userMarker: UserMarker) { current[userMarker.id]!!.position = userMarker.position }

	// TODO remove when user stops sharing

}