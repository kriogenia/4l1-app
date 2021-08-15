package dev.sotoestevez.allforone.util.extensions

import android.app.Activity
import android.util.Log
import dev.sotoestevez.allforone.BuildConfig

/**
 * Logs a debug message with the Activity name
 *
 * @param message to log
 */
fun Any.logDebug(message: String) {
	if (BuildConfig.DEBUG) Log.d(this::class.simpleName, message)
}

/**
 * Logs an info message with the Activity name
 *
 * @param message to log
 */
fun Any.logInfo(message: String) {
	Log.i(this::class.simpleName, message)
}

/**
 * Logs a warning message with the Activity name
 *
 * @param message to log
 */
fun Any.logWarning(message: String) {
	Log.d(this::class.simpleName, message)
}

/**
 * Logs an error message with the Activity name
 *
 * @param message to log
 */
fun Any.logError(message: String) {
	Log.e(this::class.simpleName, message)
}

/**
 * Logs an error message and the related error with the Activity name
 *
 * @param message to log
 * @param error to logº
 */
fun Any.logError(message: String, error: Throwable) {
	Log.e(this::class.simpleName, message, error)
}