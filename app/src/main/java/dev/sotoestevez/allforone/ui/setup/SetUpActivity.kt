package dev.sotoestevez.allforone.ui.setup

import android.app.Activity
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.databinding.ActivitySetUpBinding
import dev.sotoestevez.allforone.model.ExtendedViewModelFactory
import dev.sotoestevez.allforone.model.setup.SetUpViewModel
import dev.sotoestevez.allforone.ui.PrivateActivity
import java.util.*

/**
 * Activity to finish setting up the account of new users.
 * It features four obligatory panels:
 * 1. Name selection
 * 2. Role selection
 * 3. Extra data input
 * 4. Confirmation
 * TODO consider the option of adding Legal agreement
 */
class SetUpActivity : PrivateActivity() {

	override val model: SetUpViewModel by viewModels { ExtendedViewModelFactory(this) }

	private lateinit var binding: ActivitySetUpBinding
	private lateinit var appBarConfiguration: AppBarConfiguration

	override val roles: EnumSet<User.Role> = EnumSet.of(User.Role.BLANK)

	@Suppress("KDocMissingDocumentation")
	override fun onSupportNavigateUp(): Boolean {
		val navController = findNavController(R.id.nav_host_fragment_set_up)
		return navController.navigateUp(appBarConfiguration)
				|| super.onSupportNavigateUp()
	}

	override fun bindLayout() {
		binding = ActivitySetUpBinding.inflate(layoutInflater)
		setContentView(binding.root)

		setSupportActionBar(binding.toolbar)

		val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_set_up) as NavHostFragment
		val navController = navHostFragment.navController
		appBarConfiguration = AppBarConfiguration(navController.graph)
		setupActionBarWithNavController(navController, appBarConfiguration)
	}

	override fun attachObservers() {
		super.attachObservers()
		model.destiny.observe(this) {	nextActivity(it) }
	}

	private fun nextActivity(next: Class<out Activity>) {
		startActivity(buildIntent(next))
		finish()
	}
}