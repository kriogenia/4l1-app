package dev.sotoestevez.allforone.ui.patient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.entities.User
import dev.sotoestevez.allforone.ui.LaunchActivity
import dev.sotoestevez.allforone.util.logError
import kotlinx.android.synthetic.main.activity_pmain.*

/**
 * Main Activity of Patients
 */
class PMainActivity : AppCompatActivity() {

	/**
	 * Info of the current user
	 */
	lateinit var user: User

	/**
	 * Override of the onCreate method
	 * @param savedInstanceState bundle with a saved instance
	 */
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_pmain)
		// Retrieve user
		val fromParcelable = intent.getParcelableExtra<User>(User::class.simpleName)
		if (fromParcelable == null /*|| fromParcelable.role != User.Role.PATIENT*/) {
			// User is not logged in or has the wrong role, return to LaunchActivity
			logError("Received invalid user")
			startActivity(Intent(this, LaunchActivity::class.java))
		}
		user = fromParcelable!!
		val text = "Logged in patient: ${user.id}"
		textView.text = text
	}

}