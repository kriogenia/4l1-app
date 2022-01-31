package dev.sotoestevez.allforone.ui.activities.bonds

import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.View
import androidx.activity.viewModels
import com.google.android.gms.common.util.Strings
import dev.sotoestevez.allforone.databinding.ActivityBondsBinding
import dev.sotoestevez.allforone.ui.view.PrivateActivity
import dev.sotoestevez.allforone.ui.viewmodel.ExtendedViewModelFactory
import dev.sotoestevez.allforone.util.extensions.logDebug
import dev.sotoestevez.allforone.util.extensions.openActionConfirmationDialog
import dev.sotoestevez.allforone.util.helpers.BitmapGenerator
import dev.sotoestevez.allforone.vo.User
import java.util.*

/** Activity to list the bonds of the user and create new ones in case that the user is a [User.Role.PATIENT]*/
class BondsActivity : PrivateActivity() {

    override val model: BondsViewModel by viewModels { ExtendedViewModelFactory(this) }

    private lateinit var binding: ActivityBondsBinding

    override val roles: EnumSet<User.Role> = EnumSet.of(User.Role.PATIENT, User.Role.KEEPER)

    companion object {
        private const val QR_SIZE = 640 // pixels
    }

    override fun bindLayout() {
        binding = ActivityBondsBinding.inflate(layoutInflater)
        binding.model = model
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }

    override fun attachListeners() {
        super.attachListeners()
        binding.run {
            btnAddBond.setOnClickListener { showQrPanel(true) }
            btnCollapseQr.setOnClickListener { showQrPanel(false) }
        }
    }

    override fun attachObservers() {
        super.attachObservers()
        model.actionTaskToConfirm.observe(this) { openActionConfirmationDialog(it) }
        model.actionIntent.observe(this) { startActivity(it.build()) }
        model.qrCode.observe(this) {
            if (Strings.isEmptyOrWhitespace(it))
                return@observe
            model.loadingQr.value = false
            logDebug("Setting QR Panel")
            binding.imgQrCode.setImageBitmap(BitmapGenerator.getQrCode(it, QR_SIZE))
        }
    }

    private fun showQrPanel(expand: Boolean) {
        logDebug("Opening QR panel")
        TransitionManager.beginDelayedTransition(binding.layBonds, ChangeBounds())
        if (expand) {
            binding.run {
                layQr.visibility = View.VISIBLE
                btnAddBond.visibility = View.GONE
            }
            model.generateNewQRCode()
        } else {
            binding.run {
                layQr.visibility = View.GONE
                btnAddBond.visibility = View.VISIBLE
            }
        }
    }

}