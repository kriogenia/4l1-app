package dev.sotoestevez.allforone.model.keeper

import androidx.lifecycle.SavedStateHandle
import dev.sotoestevez.allforone.model.PrivateViewModel
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider

/** ViewModel to handle the logic of the keeper's main activity */
class KeeperMainViewModel(savedStateHandle: SavedStateHandle,
                          dispatchers: DispatcherProvider
) : PrivateViewModel(savedStateHandle, dispatchers) {
}