package dev.sotoestevez.allforone.repositories.impl

import com.google.gson.Gson
import dev.sotoestevez.allforone.api.ApiRequest
import dev.sotoestevez.allforone.api.services.FeedService
import dev.sotoestevez.allforone.repositories.NotificationRepository
import dev.sotoestevez.allforone.util.extensions.logDebug
import dev.sotoestevez.allforone.vo.Action
import dev.sotoestevez.allforone.vo.Notification

/**
 * Implementation of [NotificationRepository]
 *
 * @constructor
 *
 * @param gson   Gson serializer/deserializer
 */
class NotificationRepositoryImpl(
    private val service: FeedService,
    gson: Gson = Gson()
): BaseSocketRepository(gson), NotificationRepository {

    override suspend fun getNotifications(token: String): List<Notification> {
        logDebug("Retrieving the pending notifications of the user")
        return ApiRequest(suspend { service.getNotifications(token) }).performRequest().notifications.toList()
    }

    override fun onNotification(action: Action, callback: (Notification) -> Unit) {
        socket.on(action.path) { callback(fromJson(it, Notification::class.java)) }
    }

}