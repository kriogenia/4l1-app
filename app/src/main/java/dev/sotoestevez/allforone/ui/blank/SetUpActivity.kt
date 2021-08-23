package dev.sotoestevez.allforone.ui.blank

import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.databinding.ActivitySetUpBinding
import dev.sotoestevez.allforone.model.PrivateViewModel
import dev.sotoestevez.allforone.model.blank.SetUpViewModel
import dev.sotoestevez.allforone.model.factories.ExtendedViewModelFactory
import dev.sotoestevez.allforone.ui.PrivateActivity

/**
 * Activity to finish setting up the account of new users.
 * It features four obligatory panels:
 * 1. Name selection
 * 2. Role selection
 * 3. Extra data input
 * 4. Legal agreement
 */
class SetUpActivity : PrivateActivity() {

	/**
	 * Shared view model of the [SetUpActivity]
	 */
	override val model: SetUpViewModel by viewModels { ExtendedViewModelFactory(this) }

	private lateinit var appBarConfiguration: AppBarConfiguration
	private lateinit var binding: ActivitySetUpBinding

	override val roles: Array<User.Role> = arrayOf<User.Role>(User.Role.BLANK)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivitySetUpBinding.inflate(layoutInflater)
		setContentView(binding.root)

		setSupportActionBar(binding.toolbar)

		val navController = findNavController(R.id.nav_host_fragment_content_set_up)
		appBarConfiguration = AppBarConfiguration(navController.graph)
		setupActionBarWithNavController(navController, appBarConfiguration)

	}

	override fun onSupportNavigateUp(): Boolean {
		val navController = findNavController(R.id.nav_host_fragment_content_set_up)
		return navController.navigateUp(appBarConfiguration)
				|| super.onSupportNavigateUp()
	}
}