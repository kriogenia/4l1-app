package dev.sotoestevez.allforone.ui.activities.feed

import android.os.Bundle
import androidx.activity.viewModels
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.databinding.ActivityFeedBinding
import dev.sotoestevez.allforone.ui.viewmodel.ExtendedViewModelFactory
import dev.sotoestevez.allforone.ui.view.PrivateActivity
import dev.sotoestevez.allforone.util.extensions.logDebug
import java.util.*

/** Activity with the message Feed */
class FeedActivity : PrivateActivity() {

	companion object {
		/** Key for owner intent property */
		const val OWNER = "owner"
	}

	override val model: FeedViewModel by viewModels { ExtendedViewModelFactory(this) }

	private lateinit var binding: ActivityFeedBinding

	override val roles: EnumSet<User.Role> = EnumSet.of(User.Role.PATIENT, User.Role.KEEPER)

	@Suppress("KDocMissingDocumentation")	// override method
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		intent.getStringExtra(OWNER)?.let { title = String.format(getString(R.string.title_activity_feed), it) }
	}

	override fun bindLayout() {
		binding = ActivityFeedBinding.inflate(layoutInflater)
		binding.model = model
		binding.lifecycleOwner = this
		setContentView(binding.root)

	}

	override fun attachListeners() {
		super.attachListeners()
		binding.btnSendMsg.setOnClickListener { sendMessage() }
	}

	override fun attachObservers() {
		super.attachObservers()
		model.feedList.observe(this) {
			binding.rvFeed.post { binding.rvFeed.smoothScrollToPosition((it.size - 1).coerceAtLeast(0)) }
		}
	}

	private fun sendMessage() {
		model.sendMessage(binding.eTxtWriteMessage.text.toString())
	}

}