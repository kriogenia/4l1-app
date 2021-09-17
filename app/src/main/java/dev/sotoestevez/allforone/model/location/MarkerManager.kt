package dev.sotoestevez.allforone.model.location

import android.graphics.Color
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import dev.sotoestevez.allforone.data.UserMarker
import java.util.*
import kotlin.collections.ArrayDeque

/** Class to manage the list of markers in the location map */
class MarkerManager {

	private val current: MutableMap<String, Marker> = mutableMapOf()

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
	 * Retrieves an available color and assigns it to the maker
	 *
	 * @param marker
	 */
	fun assignColor(marker: UserMarker) {
		marker.color = Colors.pick(marker.id)
	}

	// TODO remove when user stops sharing

	internal object Colors {

		private val available: Stack<Int> = Stack()
		private val assigned: MutableMap<String, Int> = mutableMapOf()

		// TODO use my own selection of colors
		init { available.addAll(arrayOf(Color.YELLOW, Color.CYAN, Color.RED, Color.MAGENTA, Color.BLUE, Color.GREEN)) }

		fun pick(id: String): Int = available.pop().also { assigned[id] = it }

		fun release(id: String) { available.push(assigned.remove(id)) }

	}

}