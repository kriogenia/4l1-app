package dev.sotoestevez.allforone.model

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dev.sotoestevez.allforone.model.bonds.BondsViewModel
import dev.sotoestevez.allforone.model.keeper.KeeperMainViewModel
import dev.sotoestevez.allforone.model.patient.PatientMainViewModel
import dev.sotoestevez.allforone.model.setup.SetUpViewModel
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.repositories.UserRepository
import dev.sotoestevez.allforone.util.dispatcher.DefaultDispatcherProvider
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import java.util.*
import kotlin.collections.HashSet

/**
 * Factory to generate ViewModels with SavedStateHandles and SharedPreferences
 *
 * @property activity of the ViewModel
 */
class ExtendedViewModelFactory(
	val activity: AppCompatActivity
): AbstractSavedStateViewModelFactory(
	activity,
	if (activity.intent != null) activity.intent.extras else null) {

	private val wUserRepository: Set<String> = setOf(
		BondsViewModel::class.java.canonicalName!!,
		KeeperMainViewModel::class.java.canonicalName!!,
		SetUpViewModel::class.java.canonicalName!!
	)

	/**
	 * Factory method to build the desired ViewModel
	 *
	 * @param T type of the ViewModel
	 * @param key qualified name of the ViewModel
	 * @param modelClass class of the ViewModel
	 * @param handle handle of the SavedState to pass as parameter
	 * @return  expected ViewModel
	 */
	override fun <T : ViewModel?> create(
		key: String,
		modelClass: Class<T>,
		handle: SavedStateHandle
	): T {
		if (wUserRepository.contains(key.split(":")[1]))
			return modelClass
				.getConstructor(
					SavedStateHandle::class.java,
					DispatcherProvider::class.java,
					SessionRepository::class.java,
					UserRepository::class.java)
				.newInstance(handle, DefaultDispatcherProvider, SessionRepository(), UserRepository())

		return modelClass
			.getConstructor(
				SavedStateHandle::class.java,
				DispatcherProvider::class.java,
				SessionRepository::class.java)
			.newInstance(handle, DefaultDispatcherProvider, SessionRepository())
	}

}