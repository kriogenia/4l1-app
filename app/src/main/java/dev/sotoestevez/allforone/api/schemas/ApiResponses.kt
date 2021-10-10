package dev.sotoestevez.allforone.api.schemas

import dev.sotoestevez.allforone.vo.feed.TextMessage
import dev.sotoestevez.allforone.vo.Session
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.vo.feed.Message

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

/**
 * Model of the response to be received from the /tasks endpoint with the created task info
 *
 * @property _id
 * @property title
 * @property description
 * @property submitter
 * @property done
 * @property timestamp
 * @property lastUpdate
 * @property type
 */
data class TaskResponse(
	val _id: String,
	val title: String,
	val description: String?,
	val submitter: UserInfoMsg,
	val done: Boolean,
	val timestamp: Long,
	val lastUpdate: Long,
	val type: Message.Type
)