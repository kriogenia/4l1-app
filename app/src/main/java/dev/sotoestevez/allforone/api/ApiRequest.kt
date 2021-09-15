package dev.sotoestevez.allforone.api

import com.haroldadmin.cnradapter.NetworkResponse
import dev.sotoestevez.allforone.api.schemas.ErrorResponse
import dev.sotoestevez.allforone.util.extensions.logDebug
import dev.sotoestevez.allforone.util.extensions.logError
import dev.sotoestevez.allforone.util.extensions.logWarning
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
	 * @throws APIErrorException for request with non 2xx error
	 * @throws IOException for network errors
	 * @throws Throwable for unknown errors
	 */
	@Throws(APIErrorException::class, IOException::class, Throwable::class)
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
				retrieved.body?.message?.let { logWarning(it) }
				throw APIErrorException(retrieved.body)
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