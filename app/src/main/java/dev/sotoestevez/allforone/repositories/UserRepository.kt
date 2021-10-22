package dev.sotoestevez.allforone.repositories

import dev.sotoestevez.allforone.vo.User

/** Repository to make all the user related operations */
interface UserRepository {

    /**
     * Updates the user data in the server
     *
     * @param user      User data
     * @param token     Authentication token
     */
    suspend fun update(user: User, token: String)

    /**
     * Retrieves the cared of the current user if it exists, null otherwise
     *
     * @param user  Current user
     * @param token Authentication token
     * @return      Patient cared by the current user
     */
    suspend fun getCared(user: User, token: String): User?

    /**
     * Retrieves a new bonding token from the server
     *
     * @param token Authentication token
     * @return      Retrieved token
     */
    suspend fun requestBondingCode(token: String): String

    /**
     * Sends the bonding token to the server
     *
     * @param token Authentication token
     */
    suspend fun sendBondingCode(code: String, token: String)

    /**
     * Retrieves the list of bonds of the user
     *
     * @param token Authentication token
     * @return data of bonds for the user
     */
    suspend fun getBonds(token: String): List<User>

}