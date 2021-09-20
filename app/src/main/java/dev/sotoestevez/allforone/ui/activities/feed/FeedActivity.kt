package dev.sotoestevez.allforone.ui.activities.feed

import androidx.activity.viewModels
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.databinding.ActivityFeedBinding
import dev.sotoestevez.allforone.ui.viewmodel.ExtendedViewModelFactory
import dev.sotoestevez.allforone.ui.view.PrivateActivity
import java.util.*

/** Activity with the message Feed */
class FeedActivity : PrivateActivity() {

	override val model: FeedViewModel by viewModels { ExtendedViewModelFactory(this) }

	private lateinit var binding: ActivityFeedBinding

	override val roles: EnumSet<User.Role> = EnumSet.of(User.Role.PATIENT, User.Role.KEEPER)

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

	private fun sendMessage() {
		model.sendMessage(binding.txtWriteMessage.text.toString())
	}

}