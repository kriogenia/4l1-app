package dev.sotoestevez.allforone.api.services

import com.haroldadmin.cnradapter.NetworkResponse
import dev.sotoestevez.allforone.api.schemas.*
import dev.sotoestevez.allforone.data.User
import retrofit2.http.*


/** Service to handle the operations related to the /user endpoints of the API */
interface UserService {

	/**
	 * Requests to the server to establish a bond
	 *
	 * @param token Authorization token to perform the request
	 * @return      Response with the confirmation or denial message
	 */
	@POST("/user/bond/establish")
	suspend fun bondEstablish(
		@Header("Authorization") token: String,
		@Body body: BondEstablishRequest
	): NetworkResponse<MessageResponse, BaseErrorResponse>

	/**
	 * Requests to the server a new bonding token
	 *
	 * @param token Authorization token to perform the request
	 * @return      Response with the bonding token
	 */
	@GET("/user/bond/generate")
	suspend fun bondGenerate(
		@Header("Authorization") token: String
	): NetworkResponse<BondGenerateResponse, BaseErrorResponse>

	/**
	 * Requests to the server the list of bonded Users
	 *
	 * @param token Authorization token to perform the request
	 * @return      Response with the list of Users
	 */
	@GET("/user/bond/list")
	suspend fun bondList(
		@Header("Authorization") token: String
	): NetworkResponse<BondListResponse, BaseErrorResponse>


	/**
	 * Requests to the server the data of the cared user if it exists
	 *
	 * @param token Authorization token to perform the request
	 * @return      Response with the cared of the user
	 */
	@GET("/user/cared")
	suspend fun cared(
		@Header("Authorization") token: String
	): NetworkResponse<CaredResponse, BaseErrorResponse>

	/**
	 * Sends the new user data to persist it in the server
	 *
	 * @param token Authorization token to perform the request
	 * @param body  User object with all the new data
	 * @return      Response with the confirmation or denial message
	 */
	@PUT("/user/update")
	suspend fun update(
		@Header("Authorization") token: String,
		@Body body: User
	): NetworkResponse<MessageResponse, BaseErrorResponse>

}