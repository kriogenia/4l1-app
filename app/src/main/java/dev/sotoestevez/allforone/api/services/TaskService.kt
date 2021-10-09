package dev.sotoestevez.allforone.api.services

import com.haroldadmin.cnradapter.NetworkResponse
import dev.sotoestevez.allforone.api.schemas.BaseErrorResponse
import dev.sotoestevez.allforone.api.schemas.TaskListResponse
import dev.sotoestevez.allforone.api.schemas.TaskRequest
import dev.sotoestevez.allforone.api.schemas.TaskResponse
import retrofit2.http.*

/** Service to handle the operations related to the /tasks endpoints of the API */
interface TaskService {

    /**
     * Retrieves the list of relevant tasks of the user
     *
     * @param token Authorization token to perform the request
     * @return      Response with the list of tasks
     */
    @GET("/tasks")
    suspend fun getTasks(
        @Header("Authorization") token: String
    ): NetworkResponse<TaskListResponse, BaseErrorResponse>

    /**
     * Sends a new task to create to the API
     *
     * @param token Authorization token to perform the request
     * @param task  Page of messages to retrieve
     * @return      Response with the list of messages of the batch
     */
    @POST("/tasks")
    suspend fun postTask(
        @Header("Authorization") token: String,
        @Body task: TaskRequest
    ): NetworkResponse<TaskResponse, BaseErrorResponse>

    /**
     * Sets a task as done
     *
     * @param token Authorization token to perform the request
     * @param id    Unique identifier of the task
     * @return      Response of the request
     */
    @POST("/tasks/{id}/done")
    suspend fun postTaskDone(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): NetworkResponse<Unit, BaseErrorResponse>

    /**
     * Sets a task as not done
     *
     * @param token Authorization token to perform the request
     * @param id    Unique identifier of the task
     * @return      Response of the request
     */
    @DELETE("/tasks/{id}/done")
    suspend fun deleteTaskDone(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): NetworkResponse<Unit, BaseErrorResponse>

}