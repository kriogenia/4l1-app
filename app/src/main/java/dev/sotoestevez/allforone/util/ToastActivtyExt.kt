package dev.sotoestevez.allforone.util

import android.app.Activity
import android.widget.Toast

/**
 * Build and show a default toaster in the invoker activity
 *
 * @param message to print in the toast
 */
fun Activity.toast(message: String) {
	Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}