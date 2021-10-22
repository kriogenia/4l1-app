package dev.sotoestevez.allforone.repositories.impl

import dev.sotoestevez.allforone.api.ApiRequest
import dev.sotoestevez.allforone.api.schemas.BondEstablishRequest
import dev.sotoestevez.allforone.api.schemas.UserUpdateRequest
import dev.sotoestevez.allforone.api.services.UserService
import dev.sotoestevez.allforone.repositories.UserRepository
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.util.extensions.logDebug

/** Repository to make all the user related operations */
class UserRepositoryImpl(
    private val service: UserService
) : UserRepository {

    override suspend fun update(user: User, token: String) {
        logDebug("Updating user: $user")
        val data = UserUpdateRequest(
            user.role, user.displayName, user.mainPhoneNumber,
            user.altPhoneNumber, user.address, user.email
        )
        ApiRequest(suspend { service.patchUser(token, user.id!!, data) }).performRequest()
    }

    override suspend fun getCared(user: User, token: String): User? {
        logDebug("Retrieving cared user")
        val cared = ApiRequest(suspend { service.getCared(token, user.id!!) }).performRequest().cared
        logDebug("Retrieved cared: $cared")
        return cared
    }

    override suspend fun requestBondingCode(token: String): String {
        logDebug("Requesting new bonding code")
        val code = ApiRequest(suspend { service.getBondCode(token) }).performRequest().code
        logDebug("Retrieved code: $code")
        return code
    }

    override suspend fun sendBondingCode(code: String, token: String) {
        logDebug("Sending bonding code: $code")
        ApiRequest(suspend { service.postNewBond(token, BondEstablishRequest(code)) }).performRequest()
    }

    override suspend fun getBonds(token: String): List<User> {
        logDebug("Retrieving user bonds")
        val bonds = ApiRequest(suspend { service.getBonds(token) }).performRequest().bonds
        logDebug("Retrieved bonds: $bonds")
        return ArrayList(bonds.asList())
    }

}