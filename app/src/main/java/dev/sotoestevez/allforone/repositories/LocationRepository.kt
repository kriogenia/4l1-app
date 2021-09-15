package dev.sotoestevez.allforone.repositories

import android.location.Location
import com.google.gson.Gson
import dev.sotoestevez.allforone.api.schemas.LocationShare
import dev.sotoestevez.allforone.api.schemas.LocationUpdate
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.util.extensions.logDebug

/**
 * Repository to manage all the location sharing related operations
 *
 * @constructor
 *
 * @param gson   Gson serializer/deserializer
 */
class LocationRepository(gson: Gson = Gson()): BaseSocketRepository(gson) {

	/** Events managed by the Location Repository **/
	enum class Events(internal val id: String) {
		SHARE("location:share"),
		UPDATE("location:update")
	}

	/**
	 * Notifies to the server that the current user has started sharing its location
	 *
	 * @param user  current user
	 */
	fun startSharing(user: User) {
		socket.emit(Events.SHARE.id, toJson(LocationShare(user.id!!, user.displayName!!)))
		logDebug("User[${user.id}] has started sharing its location")
	}

	/**
	 * Sends the new location of the user to the server
	 *
	 * @param location  new location of the user
	 */
	fun update(user: User, location: Location) {
		val locationUpdate = LocationUpdate(user.id!!, user.displayName!!, location.latitude, location.longitude)
		socket.emit(Events.UPDATE.id, toJson(locationUpdate))
	}

}