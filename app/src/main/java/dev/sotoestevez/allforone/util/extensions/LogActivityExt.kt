package dev.sotoestevez.allforone.util.extensions

import android.util.Log
import dev.sotoestevez.allforone.BuildConfig

internal fun Any.logDebug(message: String) {
	if (BuildConfig.DEBUG) Log.d(this::class.simpleName, message)
}

internal fun Any.logInfo(message: String) {
	Log.i(this::class.simpleName, message)
}

internal fun Any.logWarning(message: String) {
	Log.d(this::class.simpleName, message)
}

internal fun Any.logError(message: String) {
	Log.e(this::class.simpleName, message)
}

internal fun Any.logError(message: String, error: Throwable) {
	Log.e(this::class.simpleName, message, error)
}