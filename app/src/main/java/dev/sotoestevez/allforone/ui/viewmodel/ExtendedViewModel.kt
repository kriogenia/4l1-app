package dev.sotoestevez.allforone.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.util.helpers.SessionManager
import dev.sotoestevez.allforone.repositories.*
import dev.sotoestevez.allforone.repositories.impl.*
import dev.sotoestevez.allforone.util.dispatcher.DefaultDispatcherProvider
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider

/** Extension of the ViewModel with common declarations */
interface ExtendedViewModel {

	/**	Module to manage the tokens currently stored in memory */
	val sessionManager: SessionManager

	/** Live data with the current user info **/
	val user: LiveData<User>

	/** Live data holding the error to handle in the Activity */
	val error: LiveData<Throwable>

	/** Mutable live data to set the activity loading state from the model or the ui */
	val loading: MutableLiveData<Boolean>

	/**
	 * Building class to serve and inject the dependencies of the ExtendedViewModels when building them on the Factory
	 *
	 * @property savedStateHandle   activity saved state handle
	 * @property dispatchers        instance of the provider of coroutine dispatchers
	 */
	data class Injector(
		val savedStateHandle: SavedStateHandle,
		val dispatchers: DispatcherProvider = DefaultDispatcherProvider
	) {

		/** Instance of a session repository */
		val sessionRepository: SessionRepository
			get() = SessionRepositoryImpl()

		/** Instance of a user repository */
		val userRepository: UserRepository
			get() = UserRepositoryImpl()

		/** Instance of a socket repository */
		val globalRoomRepository: GlobalRoomRepository
			get() = GlobalRoomRepositoryImpl()

		/** Instance of a location repository */
		val locationRepository: LocationRepository
			get() = LocationRepositoryImpl()

		/** Instance of feed repository */
		val feedRepository: FeedRepository
			get() = FeedRepositoryImpl()

		/**
		 * Builds the specified ViewModel
		 *
		 * @param T Type of the ViewModel to build
		 * @param modelClass class of the ViewModel to build
		 * @return instance of the ViewModel built
		 */
		fun <T: ViewModel?> build(modelClass: Class<T>): T {
			return modelClass.getConstructor(this::class.java).newInstance(this)
		}

	}

}