package dev.sotoestevez.allforone.api.services

import com.haroldadmin.cnradapter.NetworkResponse
import dev.sotoestevez.allforone.api.schemas.BaseErrorResponse
import dev.sotoestevez.allforone.api.schemas.FeedMessageResponse
import dev.sotoestevez.allforone.api.schemas.FeedNotificationsResponse
import retrofit2.http.*

/** Service to handle the operations related to the /feed endpoints of the API */
interface FeedService {

    /**
     * Retrieves hte pending notifications of the user from the API
     *
     * @param token    Authorization token to perform the request
     * @param maxDays  Max number of days of the notification
     * @return      Response with the list of notifications
     */
    @GET("/feed/notifications")
    suspend fun getNotifications(
        @Header("Authorization") token: String,
        @Query("maxDays") maxDays: Int? = null
    ): NetworkResponse<FeedNotificationsResponse, BaseErrorResponse>

    /**
     * Retrieves a batch of messages from the API
     *
     * @param token Authorization token to perform the request
     * @param page  Page of messages to retrieve
     * @return      Response with the list of messages of the batch
     */
    @GET("/feed/messages")
    suspend fun getMessages(
        @Header("Authorization") token: String,
        @Query("page") page: Int?
    ): NetworkResponse<FeedMessageResponse, BaseErrorResponse>

    /**
     * Post the state of a notification as read
     *
     * @param token Authorization token to perform the request
     * @param id    Unique identifier of the notification
     * @return      Response of the operation
     */
    @POST("/feed/notifications/{id}/read")
    suspend fun postNotificationsRead(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): NetworkResponse<Unit, BaseErrorResponse>

}