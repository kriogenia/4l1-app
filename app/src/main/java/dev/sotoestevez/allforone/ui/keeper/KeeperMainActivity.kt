package dev.sotoestevez.allforone.ui.keeper

import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.databinding.ActivityKeeperMainBinding
import dev.sotoestevez.allforone.model.ExtendedViewModelFactory
import dev.sotoestevez.allforone.ui.qr.QRScannerActivity
import dev.sotoestevez.allforone.model.keeper.KeeperMainViewModel
import dev.sotoestevez.allforone.ui.PrivateActivity
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

	override fun bindLayout() {
		binding = ActivityKeeperMainBinding.inflate(layoutInflater)
		setContentView(binding.root)
	}

	override fun attachListeners() {
		super.attachListeners()
		binding.btnForgeBond.setOnClickListener { invokeQRScanner() }
	}

	private fun invokeQRScanner() {
		registerForActivityResult(
			ActivityResultContracts.StartActivityForResult()
		) { result ->
			if (result.resultCode == Activity.RESULT_OK)
				model.bond(result.data?.data.toString())
			else
				toast(getString(R.string.error_qr_scanner))
		}.launch(Intent(this, QRScannerActivity::class.java))
	}

}