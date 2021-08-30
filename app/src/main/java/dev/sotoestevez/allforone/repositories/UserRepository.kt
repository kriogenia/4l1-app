package dev.sotoestevez.allforone.repositories

import dev.sotoestevez.allforone.api.ApiFactory
import dev.sotoestevez.allforone.api.ApiRequest
import dev.sotoestevez.allforone.api.requests.BondEstablishRequest
import dev.sotoestevez.allforone.api.services.UserService
import dev.sotoestevez.allforone.data.User

/** Repository to make all the user related operations */
class UserRepository(
	private val userService: UserService = ApiFactory.getUserService()
) {

	/**
	 * Retrieves a new bonding token from the server
	 *
	 * @param token Authentication token
	 * @return      Retrieved token
	 */
	suspend fun requestBondingCode(token: String): String {
		return ApiRequest(suspend { userService.bondGenerate(token) }).performRequest().code
	}

	/**
	 * Sends the bonding token to the server
	 *
	 * @param token Authentication token
	 */
	suspend fun sendBondingCode(code: String, token: String) {
		ApiRequest(suspend { userService.bondEstablish(token, BondEstablishRequest(code)) }).performRequest()
	}

	/**
	 * Updates the user data in the server
	 *
	 * @param user      User data
	 * @param token     Authentication token
	 */
	suspend fun update(user: User, token: String) {
		ApiRequest(suspend { userService.update(token, user) }).performRequest()
	}

}