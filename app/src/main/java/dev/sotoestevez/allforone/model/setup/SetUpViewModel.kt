package dev.sotoestevez.allforone.model.setup

import androidx.lifecycle.SavedStateHandle
import dev.sotoestevez.allforone.data.Address
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.model.PrivateViewModel
import dev.sotoestevez.allforone.util.dispatcher.DefaultDispatcherProvider
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import java.util.*

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
	fun setDisplayName(displayName: String): Unit {
		_user.value = _user.value?.also {
			it.displayName = displayName
		}
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

	/**
	 * Updates the main phone number of the user
	 *
	 * @param number Main phone number of the user
	 */
	fun setMainPhoneNumber(number: String) {
		_user.value?.mainPhoneNumber = number
	}

	/**
	 * Updates the alternative phone number of the user
	 *
	 * @param number Alternative phone number of the user
	 */
	fun setAltPhoneNumber(number: String) {
		_user.value?.altPhoneNumber = number
	}

	/**
	 * Updates the email address of the user
	 *
	 * @param email Email address of the user
	 */
	fun setEmail(email: String) {
		_user.value?.email = email
	}

	/**
	 * Updates the postal address of the user
	 *
	 * @param street Street and number
	 * @param door Door number
	 * @param locality Locality
	 * @param region Administrative area or region
	 */
	fun setAddress(street: String, door: String, locality: String, region: String) {
		_user.value?.address = Address(street, door, locality, region)
	}

}