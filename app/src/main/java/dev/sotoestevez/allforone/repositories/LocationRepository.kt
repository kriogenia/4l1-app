package dev.sotoestevez.allforone.repositories

import android.location.Location
import dev.sotoestevez.allforone.api.schemas.UserInfoMsg
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.vo.UserMarker

/** Repository to manage all the location sharing related operations */
interface LocationRepository: SocketRepository {

	/**
	 * Sends the new location of the user to the server
	 *
	 * @param location  new location of the user
	 */
	fun update(user: User, location: Location)

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