package dev.sotoestevez.allforone.api.services

import com.haroldadmin.cnradapter.NetworkResponse
import dev.sotoestevez.allforone.api.schemas.*
import dev.sotoestevez.allforone.vo.User
import retrofit2.http.*

/** Service to handle the operations related to the /user endpoints of the API */
interface UserService {

	/**
	 * Sends the new user data to persist it in the server
	 *
	 * @param token Authorization token to perform the request
	 * @param body  New data of the user
	 * @return      Response with the confirmation or denial message
	 */
	@PATCH("/user/{id}")
	suspend fun patchUser(
		@Header("Authorization") token: String,
		@Path("id") id: String,
		@Body body: UserUpdateRequest
	): NetworkResponse<User, BaseErrorResponse>

	/**
	 * Requests to the server the data of the cared user if it exists
	 *
	 * @param token Authorization token to perform the request
	 * @return      Response with the cared of the user
	 */
	@GET("/user/{id}/cared")
	suspend fun getCared(
		@Header("Authorization") token: String,
		@Path("id") id: String
	): NetworkResponse<CaredResponse, BaseErrorResponse>

	/**
	 * Requests to the server the list of bonded Users
	 *
	 * @param token Authorization token to perform the request
	 * @return      Response with the list of Users
	 */
	@GET("/user/bonds")
	suspend fun getBonds(
		@Header("Authorization") token: String
	): NetworkResponse<BondListResponse, BaseErrorResponse>

	/**
	 * Requests to the server to establish a bond
	 *
	 * @param token Authorization token to perform the request
	 * @return      Response with the confirmation or denial message
	 */
	@POST("/user/bonds/establish")
	suspend fun postNewBond(
		@Header("Authorization") token: String,
		@Body body: BondEstablishRequest
	): NetworkResponse<MessageResponse, BaseErrorResponse>

	/**
	 * Requests to the server a new bonding token
	 *
	 * @param token Authorization token to perform the request
	 * @return      Response with the bonding token
	 */
	@GET("/user/bonds/generate")
	suspend fun getBondCode(
		@Header("Authorization") token: String
	): NetworkResponse<BondGenerateResponse, BaseErrorResponse>

}