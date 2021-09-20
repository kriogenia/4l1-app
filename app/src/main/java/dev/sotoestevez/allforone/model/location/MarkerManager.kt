package dev.sotoestevez.allforone.model.location

import com.google.android.gms.maps.model.Marker
import dev.sotoestevez.allforone.R.color.*
import dev.sotoestevez.allforone.data.UserMarker
import java.util.*

/** Class to manage the list of markers in the location map */
class MarkerManager {

	private val current: MutableMap<String, Marker> = mutableMapOf()
	private val colors = Colors()

	/**
	 * Stores the marker in the map to keep track of it
	 *
	 * @param marker    new map marker
	 */
	fun add(marker: Marker) {
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

	/**
	 * Removes a marker from the map and release its color so it can be reused
	 *
	 * @param id id of the user
	 * @return  marker of the user
	 */
	fun remove(id: String): Marker? = current.remove(id).also { colors.release(id) }

	/**
	 * Retrieves an available color and assigns it to the maker
	 *
	 * @param marker
	 */
	fun assignColor(marker: UserMarker) { marker.color = colors.pick(marker.id) }


	internal class Colors {

		private val available: Stack<Int> = Stack()
		private val assigned: MutableMap<String, Int> = mutableMapOf()

		init { available.addAll(arrayOf(brown_marker, magenta_marker, orange_marker, green_marker, red_marker, blue_marker)) }

		fun pick(id: String): Int = available.pop().also { assigned[id] = it }

		fun release(id: String) { available.push(assigned.remove(id)) }

	}

}