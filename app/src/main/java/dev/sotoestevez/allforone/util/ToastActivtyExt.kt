package dev.sotoestevez.allforone.util

import android.app.Activity
import android.widget.Toast
import dev.sotoestevez.allforone.R

/**
 * Displays a toast with an localized error message
 *
 * @param throwable with the info to display
 */
fun Activity.errorToast(throwable: Throwable) {
	// TODO localize exception messages
	// Retrieve the message from the error
	throwable.localizedMessage
	var message = throwable.message ?: getString(R.string.error_unexpected)
	// In case of API error, get the related string resource to ensure internationalization
	val apiPrefix = getString(R.string.api_prefix)
	if (message.startsWith(apiPrefix)) {
		message = when(message.replaceFirst(apiPrefix, "")) {
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
