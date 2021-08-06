package dev.sotoestevez.allforone.api.services

import com.google.android.gms.auth.api.credentials.Credentials
import dev.sotoestevez.allforone.api.data.GoogleCredentials
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Service to handle the operations related to the /auth endpoints of the API
 */
interface AuthService {

	/**
	 * Sends the user credentials to the server to handle the log in request
	 * @return logged in user
	 */
	@POST("/auth/validate")
	suspend fun validateCredentials(@Body credentials: GoogleCredentials): Credentials

}