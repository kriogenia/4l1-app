package dev.sotoestevez.allforone.repositories

import dev.sotoestevez.allforone.api.ApiFactory
import dev.sotoestevez.allforone.api.ApiRequest
import dev.sotoestevez.allforone.api.schemas.BondEstablishRequest
import dev.sotoestevez.allforone.api.services.UserService
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.util.extensions.logDebug

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
		logDebug("Requesting new bonding code")
		val code = ApiRequest(suspend { userService.bondGenerate(token) }).performRequest().code
		logDebug("Retrieved code: $code")
		return code
	}

	/**
	 * Sends the bonding token to the server
	 *
	 * @param token Authentication token
	 */
	suspend fun sendBondingCode(code: String, token: String) {
		logDebug("Sending bonding code: $code")
		ApiRequest(suspend { userService.bondEstablish(token, BondEstablishRequest(code)) }).performRequest()
	}

	/**
	 * Retrieves the list of bonds of the user
	 *
	 * @param token Authentication token
	 * @return data of bonds for the user
	 */
	suspend fun getBonds(token: String): List<User> {
		logDebug("Retrieving user bonds")
		val bonds = ApiRequest(suspend { userService.bondList(token) }).performRequest().bonds
		logDebug("Retrieved bonds: $bonds")
		return ArrayList(bonds.asList())
	}

	/**
	 * Retrieves the cared of the current user if it exists, null otherwise
	 *
	 * @param token Authentication token
	 * @return      Patient cared by the current user
	 */
	suspend fun getCared(token: String): User? {
		logDebug("Retrieving cared user")
		val cared = ApiRequest(suspend { userService.cared(token) }).performRequest().cared
		logDebug("Retrieved cared: $cared")
		return cared
	}

	/**
	 * Updates the user data in the server
	 *
	 * @param user      User data
	 * @param token     Authentication token
	 */
	suspend fun update(user: User, token: String) {
		logDebug("Updating user: $user")
		ApiRequest(suspend { userService.update(token, user) }).performRequest()
	}

}