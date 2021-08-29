package dev.sotoestevez.allforone.ui.keeper

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.databinding.ActivityKeeperMainBinding
import dev.sotoestevez.allforone.model.ExtendedViewModelFactory
import dev.sotoestevez.allforone.model.QRScannerActivity
import dev.sotoestevez.allforone.model.keeper.KeeperMainViewModel
import dev.sotoestevez.allforone.model.patient.PatientMainViewModel
import dev.sotoestevez.allforone.ui.PrivateActivity
import java.util.*

/**
 *  Main activity of Keepers
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
		binding.btnForgeBond.setOnClickListener { startActivity(buildIntent(QRScannerActivity::class.java)) }
	}

}