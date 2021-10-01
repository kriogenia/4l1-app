package dev.sotoestevez.allforone.api.services

import com.haroldadmin.cnradapter.NetworkResponse
import dev.sotoestevez.allforone.api.schemas.BaseErrorResponse
import dev.sotoestevez.allforone.api.schemas.RefreshResponse
import dev.sotoestevez.allforone.api.schemas.SignInResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

/** Service to handle the operations related to the /auth endpoints of the API */
interface AuthService {

	/**
	 * Sends the user credentials to the server to handle the log in request
	 *
	 * @param token Google Id Token to perform the authentication
	 * @return      Response with the authentication data (session and user) or with an error
	 */
	@GET("/auth/signin/{token}")
	suspend fun signIn(
		@Path("token") token: String
	): NetworkResponse<SignInResponse, BaseErrorResponse>

	/**
	 * Sends the current session data to the server to renew the tokens
	 *
	 * @param body   Current session tokens to renew (auth and refresh token)
	 * @return       Response with the new session data
	 */
	@GET("/auth/refresh/{token}")
	suspend fun refresh(
		@Header("Authorization") auth: String,
		@Path("token") refresh: String
	): NetworkResponse<RefreshResponse, BaseErrorResponse>

}