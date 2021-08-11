package dev.sotoestevez.allforone.api

import androidx.core.app.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.haroldadmin.cnradapter.NetworkResponse
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.api.data.ErrorResponse
import dev.sotoestevez.allforone.util.logDebug
import dev.sotoestevez.allforone.util.logError
import dev.sotoestevez.allforone.util.logWarning
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Builds a request to the API and manage the realization of that request
 *
 * @param Res expected type of the API response
 * @param Err expected type of API error responses
 * @property context of the request
 * @property request to perform to the API
 */
class ApiRequest<Res : Any, Err : ErrorResponse> (
	private val context: ViewModel,
	private val request: suspend () -> NetworkResponse<Res, Err>,
) {

	/**
	 * Executes the request and manage the response received executing the callbacks in case of
	 * success or error. Also logs and manages the possible errors, known and unknown.
	 *
	 * @param onCompletion callback to perform in case of successful request
	 * @param onFailure callback to perform in case of failed request
	 */
	fun performRequest(
		onCompletion: (result: Res) -> Unit,
		onFailure: (cause: Throwable) -> Unit
	) {
		// Launch the coroutine
		context.viewModelScope.launch {
			// Performs the request
			when (val retrieved = request()) {
				// if the response is successful, calls the onCompletion function with it
				is NetworkResponse.Success -> {
					context.logDebug("Successful request: ${retrieved.code}")
					onCompletion(retrieved.body)
				}
				// error in the request, cancel the coroutine and handle it
				is NetworkResponse.ServerError -> {
					val message = if (retrieved.body?.message != null)
						"[API] ${retrieved.body?.message}"
						else "An unexpected error has occurred"
					cancel(message)
				}
				// with unknown errors, also propagate the error
				is NetworkResponse.Error -> {
					cancel("An unexpected error has occurred", retrieved.error)
				}
			}
		}.invokeOnCompletion { cause ->
			// handle the cancellation cause
			if (cause != null) {
				if (cause.cause != null) {
					// unknown error, log it with the error
					context.logError("Error occurred validating credentials", cause.cause!!)
				} else {
					// known error, log the message as warning
					context.logWarning(cause.message!!)
				}
				// finally, call the error manager callback
				onFailure(cause)
				return@invokeOnCompletion
			}
		}
	}

}