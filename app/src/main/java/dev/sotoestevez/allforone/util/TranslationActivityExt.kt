package dev.sotoestevez.allforone.util

import android.app.Activity
import dev.sotoestevez.allforone.R

/**
 * Receives a message from the API and returns the message related to it.
 * Allows API messages to be easily translate to the different languages of the app.
 *
 * @param message received from the API
 * @return app related message
 */
fun Activity.getRelatedMessage(message: String) : String {
	return when(message) {
		getString(R.string.api_invalid_google_id) -> getString(R.string.error_invalid_google_account)
		// if not message matches, return to same message
		else -> message
	}
}

// TODO give a though to all this