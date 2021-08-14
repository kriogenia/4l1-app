package dev.sotoestevez.allforone.api

import com.haroldadmin.cnradapter.NetworkResponse
import dev.sotoestevez.allforone.api.data.ErrorResponse
import dev.sotoestevez.allforone.util.logDebug
import dev.sotoestevez.allforone.util.logError
import dev.sotoestevez.allforone.util.logWarning
import java.io.IOException

/**
 * Builds a request to the API and manage the realization of that request
 *
 * @param Res expected type of the API response
 * @param Err expected type of API error responses
 * @property request to perform to the API
 */
class ApiRequest<Res : Any, Err : ErrorResponse> (
	private val request: suspend () -> NetworkResponse<Res, Err>,
) {

	/**
	 * Executes the request and manage the response received executing the callbacks in case of
	 * success or error. Also logs and manages the possible errors, known and unknown.
	 *
	 * @throws NetworkResponse.ServerError for request with non 2xx error //TODO change to new exception
	 * @throws IOException for network errors
	 * @throws Throwable for unknown errors
	 */
	suspend fun performRequest(): Res {
		// Performs the request
		return when (val retrieved = request()) {
			// if the response is successful, return the content
			is NetworkResponse.Success -> {
				logDebug("Successful request: ${retrieved.code}")
				retrieved.body
			}
			// with server errors, log it and generate the error to throw
			is NetworkResponse.ServerError -> {
				val message = if (retrieved.body?.message != null)
					"[API] ${retrieved.body?.message}"
					else "An unexpected error has occurred"
				logWarning(message)
				throw Exception(message)	// TODO create own exception
			}
			// with unknown errors, log and propagate the error
			is NetworkResponse.NetworkError -> {
				retrieved.error.message?.let { logError(it) }
				throw retrieved.error
			}
			is NetworkResponse.UnknownError -> {
				retrieved.error.message?.let { logError(it) }
				throw retrieved.error
			}
		}
	}

}