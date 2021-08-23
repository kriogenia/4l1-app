package dev.sotoestevez.allforone.model.blank

import androidx.lifecycle.SavedStateHandle
import com.google.android.gms.common.util.Strings
import dev.sotoestevez.allforone.model.PrivateViewModel
import dev.sotoestevez.allforone.util.dispatcher.DefaultDispatcherProvider
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import java.lang.IllegalArgumentException

/**
 * Shared ViewModel of the SetUpActivity and its fragments
 */
class SetUpViewModel(
	savedStateHandle: SavedStateHandle,
	dispatchers: DispatcherProvider = DefaultDispatcherProvider
): PrivateViewModel(savedStateHandle, dispatchers) {

	/**
	 * Updates the display name of the user also calling the observers
	 *
	 * @param displayName   New display name of the user
	 */
	fun setDisplayName(displayName: String) {
		if (Strings.isEmptyOrWhitespace(displayName))
			_error.postValue(IllegalArgumentException("Your display name can't be empty"))
		_user.value = _user.value?.also {
			it.displayName = displayName
		}
	}

}