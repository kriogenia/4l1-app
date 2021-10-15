package dev.sotoestevez.allforone.ui.activities.feed

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.util.Strings
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.databinding.ActivityFeedBinding
import dev.sotoestevez.allforone.ui.components.exchange.notification.NewMessageNotification
import dev.sotoestevez.allforone.ui.components.exchange.notification.TextNotification
import dev.sotoestevez.allforone.ui.components.recyclerview.BindedItemView
import dev.sotoestevez.allforone.ui.viewmodel.ExtendedViewModelFactory
import dev.sotoestevez.allforone.ui.view.PrivateActivity
import java.util.*
import kotlin.concurrent.schedule

/** Activity with the message Feed */
class FeedActivity : PrivateActivity() {

	companion object {
		private const val NOTIFICATION_DURATION = 2000L
		/** Key for owner intent property */
		const val OWNER = "owner"
	}

	override val model: FeedViewModel by viewModels { ExtendedViewModelFactory(this) }

	private lateinit var binding: ActivityFeedBinding

	override val roles: EnumSet<User.Role> = EnumSet.of(User.Role.PATIENT, User.Role.KEEPER)

	private var autoScrolling: Boolean = true

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
		binding.run {
			btnSendMsg.setOnClickListener { sendMessage() }
			btnTask.setOnClickListener { swapTaskMode() }
			rvFeed.addOnScrollListener(onScrollListener)
		}
		// TODO only send if text is present
	}

	override fun attachObservers() {
		super.attachObservers()
		model.feedList.observe(this) { autoscroll(it) }
		model.notification.observe(this) { it?.let { updateNotification(it) } }
	}

	private fun sendMessage() {
		if (model.taskMode.value == true) {
			val title = binding.eTxtWriteMessage.text.toString()
			if (Strings.isEmptyOrWhitespace(title)) return	// TODO show error of title needed
			model.sendTaskMessage(title, binding.eTxtTaskDescription.text.toString())
			binding.eTxtTaskDescription.text?.clear()
		} else {
			model.sendTextMessage(binding.eTxtWriteMessage.text.toString())
		}
		binding.eTxtWriteMessage.text?.clear()
	}

	private fun swapTaskMode() {
		model.taskMode.value = !model.taskMode.value!!
	}

	private fun autoscroll(list: List<BindedItemView>) {
		if (!autoScrolling) return	// Only move scroll when auto scrolling
		binding.rvFeed.post { binding.rvFeed.smoothScrollToPosition((list.size - 1).coerceAtLeast(0)) }
	}

	private fun updateNotification(notification: TextNotification) {
		if (notification is NewMessageNotification && autoScrolling) return	// Don't notify new messages when scrolled down
		val string = notification.getString(this)
		binding.lblFeedNotification.text = string
		Timer().schedule(NOTIFICATION_DURATION) {
			runOnUiThread { if (binding.lblFeedNotification.text == string) binding.lblFeedNotification.text = "" }
		}
	}

	/** on reaching top of the list, request to load more messages */
	private val onScrollListener = object : RecyclerView.OnScrollListener() {

		private var isFirst = true	// flag to discern call on loading

		override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
			if (isFirst) {
				isFirst = false
				return
			}
			autoScrolling = false
			// Message loading if reaching top
			if (!recyclerView.canScrollVertically(-1)) model.addMessages()
			// reaching bottom resets autoscroll
			if (!recyclerView.canScrollVertically(1)) autoScrolling = true
		}

	}

}