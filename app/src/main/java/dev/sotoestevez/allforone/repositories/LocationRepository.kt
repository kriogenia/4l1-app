package dev.sotoestevez.allforone.repositories

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import dev.sotoestevez.allforone.api.schemas.UserInfoMsg
import dev.sotoestevez.allforone.vo.UserMarker
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.util.extensions.logDebug

/** Repository to manage all the location sharing related operations */
interface LocationRepository {

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
	fun start(user: User)

	/**
	 * Sends the new location of the user to the server
	 *
	 * @param location  new location of the user
	 */
	fun update(user: User, location: Location)

	/**
	 * Notifies the server that the user will stop sharing its location
	 *
	 * @param user current user
	 */
	fun stop(user: User)

	/**
	 * Subscribes the callback to location updates received from users in the same location room
	 *
	 * @param callback  Event listener, receives the marker of the user
	 */
	fun onExternalUpdate(callback: (userMarker: UserMarker) -> Unit)

	/**
	 * Subscribes the callback to notifications of users leaving the location room
	 *
	 * @param callback Event listener, receives the data of the user
	 */
	fun onUserLeaving(callback: (userInfo: UserInfoMsg) -> Unit)

}