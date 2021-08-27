package dev.sotoestevez.allforone.ui.keeper

import android.os.Bundle
import dev.sotoestevez.allforone.databinding.ActivityKmainBinding
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.ui.PrivateActivity
import java.util.*

/**
 *  Main activity of Keepers
 */
class KMainActivity : PrivateActivity() {

	private lateinit var binding: ActivityKmainBinding

	override val roles: EnumSet<User.Role> = EnumSet.of(User.Role.KEEPER)

	/**
	 * Override of the onCreate method
	 * @param savedInstanceState bundle with a saved instance
	 */
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// Set the layout
		binding = ActivityKmainBinding.inflate(layoutInflater)
		setContentView(binding.root)
	}

	override fun bindLayout() {
		TODO("Not yet implemented")
	}

	/**
	 * Override of the onStart method
	 */
	override fun onStart() {
		super.onStart()
		val text = "Logged in keeper: ${model.user.value?.id}"
		binding.textView2.text = text
	}

}