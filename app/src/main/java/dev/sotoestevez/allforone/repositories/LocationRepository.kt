package dev.sotoestevez.allforone.repositories

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import dev.sotoestevez.allforone.api.schemas.UserInfoMsg
import dev.sotoestevez.allforone.vo.UserMarker
import dev.sotoestevez.allforone.vo.User
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
		/** Event to start sharing the user location */
		SHARE("location:share"),
		/** Event to leave the location room */
		STOP("location:stop"),
		/** Event with the location update of an user */
		UPDATE("location:update")
	}

	/**
	 * Notifies to the server that the current user has started sharing its location
	 *
	 * @param user  current user
	 */
	fun start(user: User) {
		socket.emit(Events.SHARE.id, toJson(UserInfoMsg(user.id!!, user.displayName!!)))
		logDebug("User[${user.id}] has started sharing its location")
	}

	/**
	 * Sends the new location of the user to the server
	 *
	 * @param location  new location of the user
	 */
	fun update(user: User, location: Location) {
		val locationUpdate = UserMarker(user.id!!, user.displayName!!, LatLng(location.latitude, location.longitude))
		socket.emit(Events.UPDATE.id, toJson(locationUpdate))
	}

	/**
	 * Notifies the server that the user will stop sharing its location
	 *
	 * @param user current user
	 */
	fun stop(user: User) {
		socket.emit(Events.STOP.id, toJson(UserInfoMsg(user.id!!, user.displayName!!)))
	}

	/**
	 * Subscribes the callback to location updates received from users in the same location room
	 *
	 * @param callback  Event listener, receives the marker of the user
	 */
	fun onExternalUpdate(callback: (userMarker: UserMarker) -> Unit) {
		socket.on(Events.UPDATE.id) { callback(fromJson(it, UserMarker::class.java)) }
	}

	/**
	 * Subscribes the callback to notifications of users leaving the location room
	 *
	 * @param callback Event listener, receives the data of the user
	 */
	fun onUserLeaving(callback: (userInfo: UserInfoMsg) -> Unit) {
		socket.on(Events.STOP.id) { callback(fromJson(it, UserInfoMsg::class.java)) }
	}

}