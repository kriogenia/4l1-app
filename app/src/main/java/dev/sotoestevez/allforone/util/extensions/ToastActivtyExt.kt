package dev.sotoestevez.allforone.util.extensions

import android.app.Activity
import android.widget.Toast
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.api.APIErrorException

/**
 * Displays a toast with an localized error message
 *
 * @param throwable with the info to display
 */
fun Activity.errorToast(throwable: Throwable) {
	// TODO localize exception messages
	// Retrieve the message from the error
	throwable.localizedMessage
	var message = throwable.localizedMessage ?: getString(R.string.error_unexpected)
	// In case of API error, get the related string resource to ensure internationalization
	if (throwable is APIErrorException) {
		message = when(message) {
			getString(R.string.api_invalid_google_id) -> getString(R.string.error_invalid_google_account)
			// if not message matches, return to same message
			else -> message
		}
	}
	// Display the toast with the message
	toast(message)
}

/**
 * Build and show a default toaster in the invoker activity
 *
 * @param message to print in the toast
 */
fun Activity.toast(message: String) {
	runOnUiThread { Toast.makeText(this, message, Toast.LENGTH_LONG).show() }
}
