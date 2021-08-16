package dev.sotoestevez.allforone.api.data

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