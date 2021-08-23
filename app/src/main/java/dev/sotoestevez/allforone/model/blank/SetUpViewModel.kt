package dev.sotoestevez.allforone.model.blank

import androidx.lifecycle.SavedStateHandle
import dev.sotoestevez.allforone.model.PrivateViewModel
import dev.sotoestevez.allforone.util.dispatcher.DefaultDispatcherProvider
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider

class SetUpViewModel(
	savedStateHandle: SavedStateHandle,
	dispatchers: DispatcherProvider = DefaultDispatcherProvider
): PrivateViewModel(savedStateHandle, dispatchers) {



}