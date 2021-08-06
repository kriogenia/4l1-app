package dev.sotoestevez.allforone.util

import android.app.Activity
import android.util.Log
import dev.sotoestevez.allforone.BuildConfig

fun Activity.logDebug(message: String) {
	if (BuildConfig.DEBUG) Log.d(this::class.simpleName, message)
}

fun Activity.logError(message: String, error: Throwable) {
	Log.e(this::class.simpleName, message, error)
}

fun Activity.logInfo(message: String) {
	Log.i(this::class.simpleName, message)
}

fun Activity.logWarning(message: String) {
	Log.d(this::class.simpleName, message)
}