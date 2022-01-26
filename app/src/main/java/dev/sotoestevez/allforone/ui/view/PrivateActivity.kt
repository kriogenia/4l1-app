package dev.sotoestevez.allforone.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.ui.activities.launch.LaunchActivity
import dev.sotoestevez.allforone.util.extensions.errorToast
import dev.sotoestevez.allforone.util.extensions.logError
import java.util.*

/**
 * Base Activity for all the Activities requiring user checking
 */
abstract class PrivateActivity : BaseExtendedActivity() {

    /** List of permitted roles in the Activity */
    abstract val roles: EnumSet<User.Role>

    @Suppress("KDocMissingDocumentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        checkUser()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = true.also { onBackPressed() }

    override fun bindLayout() {}

    override fun attachListeners() {}

    override fun attachObservers() {
        model.error.observe(this, { handleError(it) })
        model.user.observe(this, {
            if (it == null) {
                closeSession()
            }
        })
    }

    override fun handleError(error: Throwable) {
        errorToast(error)
    }

    /**
     * Retrieves the user from the intent and checks if it's valid and has valid permissions
     *
     * @return the user if it's valid
     */
    private fun checkUser() {
        val user = model.user.value
        if (user == null || !roles.contains(user.role)) {
            logError(getString(R.string.error_invalid_permissions))
            closeSession()
        }
    }

    /**
     * Closes the activity and returns to the [LaunchActivity]
     */
    private fun closeSession() {
        startActivity(Intent(this, LaunchActivity::class.java))
        finish()
    }

}