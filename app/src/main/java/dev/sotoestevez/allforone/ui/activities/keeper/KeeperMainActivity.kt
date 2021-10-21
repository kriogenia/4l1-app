package dev.sotoestevez.allforone.ui.activities.keeper

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.databinding.ActivityKeeperMainBinding
import dev.sotoestevez.allforone.ui.viewmodel.ExtendedViewModelFactory
import dev.sotoestevez.allforone.ui.activities.keeper.qr.QRScannerActivity
import dev.sotoestevez.allforone.ui.view.PrivateActivity
import dev.sotoestevez.allforone.ui.activities.bonds.BondsActivity
import dev.sotoestevez.allforone.ui.activities.feed.FeedActivity
import dev.sotoestevez.allforone.ui.activities.location.LocationActivity
import dev.sotoestevez.allforone.ui.activities.tasks.TasksActivity
import dev.sotoestevez.allforone.ui.activities.tasks.fragments.CreateTaskDialog
import dev.sotoestevez.allforone.ui.components.fragments.notifications.NotificationsDialog
import dev.sotoestevez.allforone.util.extensions.logDebug
import dev.sotoestevez.allforone.util.extensions.toast
import java.util.*

/**
 *  Main activity of Keepers.
 *  Handles the bonding with the Patient when no bond is set invoking the QR Scanner and connecting with the server
 */
class KeeperMainActivity : PrivateActivity() {

	override val model: KeeperMainViewModel by viewModels { ExtendedViewModelFactory(this) }

	private lateinit var binding: ActivityKeeperMainBinding

	override val roles: EnumSet<User.Role> = EnumSet.of(User.Role.KEEPER)

	private lateinit var qrScannerLauncher: ActivityResultLauncher<Intent>

	override fun bindLayout() {
		binding = ActivityKeeperMainBinding.inflate(layoutInflater)
		binding.model = model
		binding.lifecycleOwner = this
		setContentView(binding.root)
	}

	override fun attachListeners() {
		super.attachListeners()
		/* With bond listeners */
		binding.buttons.run {
			btnBonds.setOnClickListener { startActivity(buildIntent(BondsActivity::class.java)) }
			btnLocation.setOnClickListener { startActivity(buildIntent(LocationActivity::class.java)) }
			btnTasks.setOnClickListener { startActivity(buildIntent(TasksActivity::class.java)) }
			btnFeed.setOnClickListener {
				startActivity(buildIntent(FeedActivity::class.java).apply {
					putExtra(FeedActivity.OWNER, model.cared.value!!.displayName)
				})
			}
		}
		binding.btnNotifications.setOnClickListener { openNotificationsDialog() }
		/* No bond listeners */
		qrScannerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
		{ result ->
			if (result.resultCode == Activity.RESULT_OK)
				model.bond(result.data?.data.toString())
			else
				toast(getString(R.string.error_qr_scanner))
		}
		binding.btnForgeBond.setOnClickListener {
			logDebug("Launching QR Scanner")
			qrScannerLauncher.launch(Intent(this, QRScannerActivity::class.java))
		}
	}

	override fun attachObservers() {
		super.attachObservers()
		model.destiny.observe(this) { startActivity(buildIntent(it)) }
	}

	private fun openNotificationsDialog() {
		NotificationsDialog(model.notificationManager)
			.show(supportFragmentManager, NotificationsDialog.TAG)
	}

}