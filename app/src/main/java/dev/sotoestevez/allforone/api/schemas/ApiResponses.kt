package dev.sotoestevez.allforone.api.schemas

import dev.sotoestevez.allforone.data.Session
import dev.sotoestevez.allforone.data.User

/**
 * Base properties of error responses
 *
 * @property message with the error info
 */
interface ErrorResponse {
	val message: String
}

/**
 * Default error response from the API
 *
 * @property message with the error info
 */
data class BaseErrorResponse(override val message: String) : ErrorResponse

/**
 * Model of all the responses with only a confirmation message in the body
 *
 * @property message    Returned message
 */
data class MessageResponse(
	val message: String
)

/**
 * Model of the response to be received from the /user/cared endpoint
 *
 * @property cared  Patient cared by the current user if it exists
 */
data class CaredResponse(
	val cared: User?
)

/**
 * Model of the response to be received from the /user/bond/generate endpoint
 *
 * @property code    Returned bonding token
 */
data class BondGenerateResponse(
	val code: String
)

/**
 * Model of the response to be received from the /user/bond/generate endpoint
 *
 * @property bonds    List of bonded users
 */
data class BondListResponse(
	val bonds: Array<User>
) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as BondListResponse

		if (!bonds.contentEquals(other.bonds)) return false

		return true
	}

	override fun hashCode(): Int {
		return bonds.contentHashCode()
	}
}

/**
 * Model of the response to be received from the /auth/refresh endpoint
 *
 * @property session    Session info
 */
data class RefreshResponse(
	val session: Session
)

/**
 * Model of the response to be received from the /auth/signin endpoint
 *
 * @property session    Session info
 * @property user       Data of the user
 */
data class SignInResponse(
	val session: Session,
	val user: User
)