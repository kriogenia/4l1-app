package dev.sotoestevez.allforone.ui

import android.app.Activity
import android.content.Intent
import dev.sotoestevez.allforone.model.ExtendedViewModel

/** Extension to the Activities with common operations to all the application activities */
interface ExtendedActivity {

	/** Model to handle the logic of the Activity */
	val model: ExtendedViewModel

	/** Operation to bind the layout of the activity */
	fun bindLayout()

	/** Operation to attach the listeners to the UI components */
	fun attachListeners()

	/** Operation to attach the observers to the view model live data */
	fun attachObservers()

	/**
	 * Builds a new intent with all the commonly available info to start the desired activity
	 *
	 * @param next Next activity to launch
	 */
	fun buildIntent(next: Class<out Activity>): Intent

	/**
	 * Error handling function to manage known errors
	 *
	 * @param error Registered error to handle
	 */
	fun handleError(error: Throwable)

}