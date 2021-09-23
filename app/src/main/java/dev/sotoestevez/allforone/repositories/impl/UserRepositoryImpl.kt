package dev.sotoestevez.allforone.repositories.impl

import dev.sotoestevez.allforone.api.ApiFactory
import dev.sotoestevez.allforone.api.ApiRequest
import dev.sotoestevez.allforone.api.schemas.BondEstablishRequest
import dev.sotoestevez.allforone.api.services.UserService
import dev.sotoestevez.allforone.repositories.UserRepository
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.util.extensions.logDebug

/** Repository to make all the user related operations */
class UserRepositoryImpl(
	private val userService: UserService = ApiFactory.getUserService()
) : UserRepository {

	override suspend fun requestBondingCode(token: String): String {
		logDebug("Requesting new bonding code")
		val code = ApiRequest(suspend { userService.bondGenerate(token) }).performRequest().code
		logDebug("Retrieved code: $code")
		return code
	}

	override suspend fun sendBondingCode(code: String, token: String) {
		logDebug("Sending bonding code: $code")
		ApiRequest(suspend { userService.bondEstablish(token, BondEstablishRequest(code)) }).performRequest()
	}

	override suspend fun getBonds(token: String): List<User> {
		logDebug("Retrieving user bonds")
		val bonds = ApiRequest(suspend { userService.bondList(token) }).performRequest().bonds
		logDebug("Retrieved bonds: $bonds")
		return ArrayList(bonds.asList())
	}

	override suspend fun getCared(token: String): User? {
		logDebug("Retrieving cared user")
		val cared = ApiRequest(suspend { userService.cared(token) }).performRequest().cared
		logDebug("Retrieved cared: $cared")
		return cared
	}

	override suspend fun update(user: User, token: String) {
		logDebug("Updating user: $user")
		ApiRequest(suspend { userService.update(token, user) }).performRequest()
	}

}