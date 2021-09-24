package dev.sotoestevez.allforone.ui.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.sotoestevez.allforone.vo.Session
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.util.extensions.errorToast
import dev.sotoestevez.allforone.util.extensions.logDebug

/** Base activity to reduce code duplication and implement the common creation flow */
abstract class BaseExtendedActivity : AppCompatActivity(), ExtendedActivity, SteppedCreation {

	@Suppress("KDocMissingDocumentation")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		bindLayout()
		attachListeners()
		attachObservers()
		logDebug("Launching ${this::class.simpleName}")
	}

	override fun buildIntent(next: Class<out Activity>): Intent {
		logDebug("Building Intent for $next")
		val intent = Intent(this, next)
		intent.putExtra(Session::class.simpleName, model.sessionManager.getSession())
		intent.putExtra(User::class.simpleName, model.user.value)
		return intent
	}

	/**
	 * Base error handling function, shows a toast with the correspondent string
	 *
	 * @param error Registered error to handle
	 */
	override fun handleError(error: Throwable) {
		errorToast(error)
	}

}