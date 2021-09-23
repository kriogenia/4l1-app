package dev.sotoestevez.allforone.repositories.impl

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import dev.sotoestevez.allforone.api.schemas.UserInfoMsg
import dev.sotoestevez.allforone.repositories.LocationRepository
import dev.sotoestevez.allforone.vo.UserMarker
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.util.extensions.logDebug

/**
 * Implementation of [LocationRepository]
 *
 * @constructor
 *
 * @param gson   Gson serializer/deserializer
 */
class LocationRepositoryImpl(gson: Gson = Gson()): BaseSocketRepository(gson), LocationRepository {

	override fun start(user: User) {
		socket.emit(LocationRepository.Events.SHARE.id, toJson(user.minInfo))
		logDebug("User[${user.id}] has started sharing its location")
	}

	override fun update(user: User, location: Location) {
		// TODO convert UserMarker to use user instead of the spread properties
		val locationUpdate = UserMarker(user.id!!, user.displayName!!, LatLng(location.latitude, location.longitude))
		socket.emit(LocationRepository.Events.UPDATE.id, toJson(locationUpdate))
	}

	override fun stop(user: User) {
		socket.emit(LocationRepository.Events.STOP.id, toJson(user.minInfo))
	}

	override fun onExternalUpdate(callback: (userMarker: UserMarker) -> Unit) {
		socket.on(LocationRepository.Events.UPDATE.id) { callback(fromJson(it, UserMarker::class.java)) }
	}

	override fun onUserLeaving(callback: (userInfo: UserInfoMsg) -> Unit) {
		socket.on(LocationRepository.Events.STOP.id) { callback(fromJson(it, UserInfoMsg::class.java)) }
	}

}