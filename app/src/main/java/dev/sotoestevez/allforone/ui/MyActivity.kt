package dev.sotoestevez.allforone.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.sotoestevez.allforone.util.extensions.errorToast

/** Base activity to reduce code duplication and implement the common creation flow */
abstract class MyActivity : AppCompatActivity()  {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		bindLayout()
		attachListeners()
		attachObservers()
	}

	/** Operation to bind the layout of the activity */
	protected abstract fun bindLayout()

	/** Operation to attach the listeners to the UI components */
	protected abstract fun attachListeners()

	/** Operation to attach the observers to the view model live data */
	protected abstract fun attachObservers()

	/**
	 * Base error handling function, shows a toast with the correspondent string
	 *
	 * @param error Registered error to handle
	 */
	protected open fun handleError(error: Throwable) {
		errorToast(error)
	}

}