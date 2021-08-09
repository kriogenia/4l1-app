package dev.sotoestevez.allforone.api.services

import com.haroldadmin.cnradapter.NetworkResponse
import dev.sotoestevez.allforone.api.data.BaseErrorResponse
import dev.sotoestevez.allforone.api.data.SignInResponse
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Service to handle the operations related to the /auth endpoints of the API
 */
interface AuthService {

	/**
	 * Sends the user credentials to the server to handle the log in request
	 * @return logged in user
	 */
	@GET("/auth/signin/{token}")
	suspend fun signIn(@Path("token") token: String): NetworkResponse<SignInResponse, BaseErrorResponse>

}