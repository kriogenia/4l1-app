package dev.sotoestevez.allforone.model.blank

import androidx.lifecycle.SavedStateHandle
import com.google.android.gms.common.util.Strings
import dev.sotoestevez.allforone.data.User
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
	fun setDisplayName(displayName: String): Boolean {
		if (Strings.isEmptyOrWhitespace(displayName)) {
			_error.postValue(IllegalArgumentException("Your display name can't be empty"))
			return false
		}
		_user.value?.displayName = displayName
		return true
	}

	/**
	 * Updates the role of the user also calling the observers
	 *
	 * @param role   New role of the user
	 */
	fun setRole(role: User.Role) {
		_user.value = _user.value?.also {
			it.role = role
		}
	}

}