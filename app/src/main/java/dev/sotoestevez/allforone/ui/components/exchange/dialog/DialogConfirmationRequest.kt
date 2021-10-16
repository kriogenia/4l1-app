package dev.sotoestevez.allforone.ui.components.exchange.dialog

import android.content.Context

/** Model to invoke confirmation dialogs for the intended action */
sealed interface DialogConfirmationRequest {

    /** Action to perform in case of confirmation */
    fun onConfirm()

    /**
     * Retrieve the title of the dialog
     *
     * @param context   Activity context
     * @return          Title
     */
    fun getTitle(context: Context): String

    /**
     * Retrieve the title of the dialog
     *
     * @param context   Activity context
     * @return          Title
     */
    fun getMessage(context: Context): String

}