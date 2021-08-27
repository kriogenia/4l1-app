package dev.sotoestevez.allforone.api.services

import com.haroldadmin.cnradapter.NetworkResponse
import dev.sotoestevez.allforone.api.responses.BaseErrorResponse
import dev.sotoestevez.allforone.api.responses.MessageResponse
import dev.sotoestevez.allforone.data.User
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PUT


/** Service to handle the operations related to the /user endpoints of the API */
interface UserService {

	/**
	 * Sends the new user data to persist it in the server
	 *
	 * @param token Authorization token to perform the request
	 * @param user  User object with all the new data
	 * @return      Response with the authentication data (session and user) or with an error
	 */
	@PUT("/user/update")
	suspend fun update(
		@Header("Authorization") token: String,
		@Body user: User
	): NetworkResponse<MessageResponse, BaseErrorResponse>

}