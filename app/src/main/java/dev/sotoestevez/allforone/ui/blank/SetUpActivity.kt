package dev.sotoestevez.allforone.ui.blank

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.databinding.ActivitySetUpBinding

/**
 * Activity to finish setting up the account of new users.
 * It features two obligatory panels:
 * 1. Name and role selection
 * 2. Legal advisement
 */
class SetUpActivity : AppCompatActivity() {

	private lateinit var appBarConfiguration: AppBarConfiguration
	private lateinit var binding: ActivitySetUpBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivitySetUpBinding.inflate(layoutInflater)
		setContentView(binding.root)

		setSupportActionBar(binding.toolbar)

		val navController = findNavController(R.id.nav_host_fragment_content_set_up)
		appBarConfiguration = AppBarConfiguration(navController.graph)
		setupActionBarWithNavController(navController, appBarConfiguration)

		binding.fab.setOnClickListener { view ->
			Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
				.setAction("Action", null).show()
		}
	}

	override fun onSupportNavigateUp(): Boolean {
		val navController = findNavController(R.id.nav_host_fragment_content_set_up)
		return navController.navigateUp(appBarConfiguration)
				|| super.onSupportNavigateUp()
	}
}