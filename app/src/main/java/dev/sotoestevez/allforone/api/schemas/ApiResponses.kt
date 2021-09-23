package dev.sotoestevez.allforone.api.schemas

import dev.sotoestevez.allforone.vo.Message
import dev.sotoestevez.allforone.vo.Session
import dev.sotoestevez.allforone.vo.User
import java.sql.Timestamp

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
 * Model of the response to be received from the /user/bond/generate endpoint
 *
 * @property code    Returned bonding token
 */
data class BondGenerateResponse(
	val code: String
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