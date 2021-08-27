package dev.sotoestevez.allforone.repositories

import dev.sotoestevez.allforone.api.ApiFactory
import dev.sotoestevez.allforone.api.ApiRequest
import dev.sotoestevez.allforone.api.responses.MessageResponse
import dev.sotoestevez.allforone.data.User

/** Repository to make all the user related operations */
object UserRepository {

	/**
	 * Updates the user data in the server
	 *
	 * @param user      User data
	 * @param token     Authentication token
	 * @return          Response of the server
	 */
	suspend fun update(user: User, token: String): MessageResponse {
		val service = ApiFactory.getUserService()
		return ApiRequest(suspend { service.update(token, user) }).performRequest()
	}

}