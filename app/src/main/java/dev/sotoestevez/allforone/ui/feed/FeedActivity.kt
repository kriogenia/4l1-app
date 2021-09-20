package dev.sotoestevez.allforone.ui.feed

import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import dev.sotoestevez.allforone.data.Message
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.databinding.ActivityBondsBinding
import dev.sotoestevez.allforone.databinding.ActivityFeedBinding
import dev.sotoestevez.allforone.model.ExtendedViewModel
import dev.sotoestevez.allforone.model.ExtendedViewModelFactory
import dev.sotoestevez.allforone.model.feed.FeedViewModel
import dev.sotoestevez.allforone.ui.PrivateActivity
import java.util.*
import kotlin.collections.ArrayList

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