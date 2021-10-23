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
class LocationRepositoryImpl(gson: Gson = Gson()) : BaseSocketRepository(gson), LocationRepository {

    /** Events managed by the Location Repository **/
    enum class Events(internal val path: String) {
        /** Event to start sharing the user location */
        SHARE("location:share"),

        /** Event to leave the location room */
        STOP("location:stop"),

        /** Event with the location update of an user */
        UPDATE("location:update")
    }

    override fun join(user: User) {
        socket.emit(Events.SHARE.path, toJson(user.minInfo))
        logDebug("User[${user.id}] has started sharing its location")
    }

    override fun update(user: User, location: Location) {
        // TODO convert UserMarker to use user info instead of the spread properties
        val locationUpdate = UserMarker(user.id!!, user.displayName!!, LatLng(location.latitude, location.longitude))
        socket.emit(Events.UPDATE.path, toJson(locationUpdate))
    }

    override fun leave(user: User) {
        socket.emit(Events.STOP.path, toJson(user.minInfo))
    }

    override fun onExternalUpdate(callback: (userMarker: UserMarker) -> Unit) {
        socket.on(Events.UPDATE.path) { callback(fromJson(it, UserMarker::class.java)) }
    }

    override fun onUserLeaving(callback: (userInfo: UserInfoMsg) -> Unit) {
        socket.on(Events.STOP.path) { callback(fromJson(it, UserInfoMsg::class.java)) }
    }

}