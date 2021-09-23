package dev.sotoestevez.allforone.api.services

import com.haroldadmin.cnradapter.NetworkResponse
import dev.sotoestevez.allforone.api.schemas.BaseErrorResponse
import dev.sotoestevez.allforone.api.schemas.FeedMessageResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

/** Service to handle the operations related to the /feed endpoints of the API */
interface FeedService {

    /**
     * Retrieves a batch of messages from the API
     *
     * @param token Authorization token to perform the request
     * @param page  Page of messages to retrieve
     * @return      Response with the list of messages of the batch
     */
    @GET("/feed/messages/{page}")
    suspend fun messages(
        @Header("Authorization") token: String,
        @Path("page") page: Int?
    ): NetworkResponse<FeedMessageResponse, BaseErrorResponse>

}