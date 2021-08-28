package dev.sotoestevez.allforone.ui.bonds

import android.transition.AutoTransition
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.View
import androidx.activity.viewModels
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.databinding.ActivityBondsBinding
import dev.sotoestevez.allforone.model.ExtendedViewModelFactory
import dev.sotoestevez.allforone.model.bonds.BondsViewModel
import dev.sotoestevez.allforone.ui.PrivateActivity
import java.util.*

/** Activity to list the bonds of the user and create new ones in case that the user is a [User.Role.PATIENT]*/
class BondsActivity : PrivateActivity() {

	override val model: BondsViewModel by viewModels { ExtendedViewModelFactory(this) }

	override val roles: EnumSet<User.Role> = EnumSet.of(User.Role.PATIENT, User.Role.KEEPER)

	private lateinit var binding: ActivityBondsBinding

	override fun bindLayout() {
		binding = ActivityBondsBinding.inflate(layoutInflater)
		setContentView(binding.root)
	}

	override fun attachObservers() {
		super.attachObservers()
		binding.btnAddBond.setOnClickListener {
			showQrPanel(true)
			binding.imgQrCode.setImageBitmap(model.getQrCodeBitmap("Prueba"))
		}
		binding.btnCollapseQe.setOnClickListener { showQrPanel(false) }
	}

	private fun showQrPanel(expand: Boolean) {
		TransitionManager.beginDelayedTransition(binding.root, ChangeBounds())
		if (expand) {
			binding.layQr.visibility = View.VISIBLE
			binding.btnAddBond.visibility = View.GONE
		} else {
			binding.layQr.visibility = View.GONE
			binding.btnAddBond.visibility = View.VISIBLE

		}
	}

}