package dev.sotoestevez.allforone.util

import android.app.Activity
import android.widget.Toast
import dev.sotoestevez.allforone.R

/**
 * Displays a toast with an error message
 *
 * @param throwable with the info to display
 */
fun Activity.errorToast(throwable: Throwable) {
	// TODO retrieve error and get the localized message
	toast(throwable.message ?: getString(R.string.error_unexpected))
}

/**
 * Build and show a default toaster in the invoker activity
 *
 * @param message to print in the toast
 */
fun Activity.toast(message: String) {
	Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}