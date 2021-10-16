package dev.sotoestevez.allforone.util.extensions

import android.app.Activity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.ui.components.exchange.dialog.DialogConfirmationRequest

/**
 * Extension activity that opens a alert dialog to confirm and action with the specified properties
 *
 * @param request   Dialog properties and actions
 */
fun Activity.openActionConfirmationDialog(request: DialogConfirmationRequest) {
    MaterialAlertDialogBuilder(this)
        .setTitle(request.getTitle(this))
        .setMessage(request.getMessage(this))
        .setNegativeButton(getString(R.string.cancel)) { _, _ -> logDebug("Canceled action request") }
        .setPositiveButton(getString(R.string.confirm)) { _, _ -> request.onConfirm() }
        .show()
}