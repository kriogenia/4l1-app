package dev.sotoestevez.allforone.model.keeper

import androidx.lifecycle.SavedStateHandle
import dev.sotoestevez.allforone.model.PrivateViewModel
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import dev.sotoestevez.allforone.util.extensions.logDebug

/** ViewModel to handle the logic of the keeper's main activity */
class KeeperMainViewModel(savedStateHandle: SavedStateHandle,
                          dispatchers: DispatcherProvider
) : PrivateViewModel(savedStateHandle, dispatchers) {

    /**
     * Sends the scanned code to the server to bond with the user that provided the code
     *
     * @param code  Scanned code to perform the bond operation
     */
    fun bond(code: String) {
        logDebug("[${user.value?.id}] scanned QR Code: ${code.substring(0,6)}...")
    }

}