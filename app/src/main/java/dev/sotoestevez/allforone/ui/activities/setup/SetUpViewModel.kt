package dev.sotoestevez.allforone.ui.activities.setup

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dev.sotoestevez.allforone.vo.Address
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.ui.viewmodel.ExtendedViewModel
import dev.sotoestevez.allforone.ui.viewmodel.PrivateViewModel
import dev.sotoestevez.allforone.ui.viewmodel.WithProfileCard
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.repositories.UserRepository
import dev.sotoestevez.allforone.ui.activities.keeper.KeeperMainActivity
import dev.sotoestevez.allforone.ui.activities.patient.PatientMainActivity
import dev.sotoestevez.allforone.util.dispatcher.DefaultDispatcherProvider
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import dev.sotoestevez.allforone.util.extensions.logDebug
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalStateException

/** Shared ViewModel of the SetUpActivity and its fragments */
class SetUpViewModel(
	savedStateHandle: SavedStateHandle,
	dispatchers: DispatcherProvider = DefaultDispatcherProvider,
	sessionRepository: SessionRepository = SessionRepository(),
	private val userRepository: UserRepository = UserRepository()
): PrivateViewModel(savedStateHandle, dispatchers, sessionRepository), WithProfileCard {

	/** Live data to invoke a change of activity in the related activity **/
	val destiny: LiveData<Class<out Activity>>
		get() = mDestiny
	private var mDestiny = MutableLiveData<Class<out Activity>>()

	/** Live data holding the currently selected role of the user */
	val selectedRole: LiveData<User.Role>
		get() = mSelectedRole
	private var mSelectedRole = MutableLiveData(User.Role.BLANK)

	// WithProfileCard
	override val profileCardExpandable: Boolean = false
	override val profileCardWithBanner: Boolean = true
	override val profileCardExpanded: MutableLiveData<Boolean> = MutableLiveData(true)


	constructor(injector: ExtendedViewModel.Injector): this(
		injector.savedStateHandle,
		injector.dispatchers,
		injector.sessionRepository,
		injector.userRepository
	)


	/**
	 * Updates the display name of the user also calling the observers
	 *
	 * @param displayName   New display name of the user
	 */
	fun setDisplayName(displayName: String) {
		mUser.value = mUser.value?.also {
			it.displayName = displayName
		}
	}

	/**
	 * Updates the role of the user also calling the observers
	 *
	 * @param role   New role of the user
	 */
	fun setRole(role: User.Role) {
		mSelectedRole.value = role
		mUser.value = mUser.value?.also {
			it.role = role
		}
	}

	/**
	 * Updates the main phone number of the user
	 *
	 * @param number Main phone number of the user
	 */
	fun setMainPhoneNumber(number: String) {
		mUser.value?.mainPhoneNumber = number
	}

	/**
	 * Updates the alternative phone number of the user
	 *
	 * @param number Alternative phone number of the user
	 */
	fun setAltPhoneNumber(number: String) {
		mUser.value?.altPhoneNumber = number
	}

	/**
	 * Updates the email address of the user
	 *
	 * @param email Email address of the user
	 */
	fun setEmail(email: String) {
		mUser.value?.email = email
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
		mUser.value?.address = Address(street, door, locality, region)
	}

	/**
	 * Sends the user updated data to the server and sets an activity to launch
	 */
	fun sendUpdate() {
		logDebug("[${user.value?.id}] Finalized user set-up")
		loading.value = true
		viewModelScope.launch(dispatchers.io() + coroutineExceptionHandler) {
			val user = user.value ?: throw IllegalStateException("Set-up completed without user object")
			userRepository.update(user, authHeader())
			withContext(dispatchers.main()) {
				updateDestiny()
			}
		}
	}

	private fun updateDestiny() {
		mDestiny.value = when (user.value?.role) {
			User.Role.KEEPER -> KeeperMainActivity::class.java
			User.Role.PATIENT -> PatientMainActivity::class.java
			else -> throw IllegalStateException("Set-up completed with no role selected")
		}
	}

}