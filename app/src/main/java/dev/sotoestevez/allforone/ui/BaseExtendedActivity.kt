package dev.sotoestevez.allforone.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.sotoestevez.allforone.data.Session
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.ui.interfaces.SteppedCreation
import dev.sotoestevez.allforone.util.extensions.errorToast

/** Base activity to reduce code duplication and implement the common creation flow */
abstract class BaseExtendedActivity : AppCompatActivity(), ExtendedActivity, SteppedCreation {

	@Suppress("KDocMissingDocumentation")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		bindLayout()
		attachListeners()
		attachObservers()
	}

	override fun buildIntent(next: Class<out Activity>): Intent {
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