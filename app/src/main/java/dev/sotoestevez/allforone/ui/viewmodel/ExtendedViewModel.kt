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

    /** Building class to serve and inject the dependencies of the ExtendedViewModels when building them on the Factory */
    class Builder {

        /** Activity saved state handle */
        lateinit var savedStateHandle: SavedStateHandle

        /** Instance of the provider of coroutine dispatchers */
        val dispatchers: DispatcherProvider = DefaultDispatcherProvider

        /** Context instance of [FeedRepository] */
        val feedRepository: FeedRepository by lazy { RepositoryContext.feedRepository }

        /** Context instance of [GlobalRoomRepository] */
        val globalRoomRepository: GlobalRoomRepository by lazy { RepositoryContext.globalRoomRepository }

        /** Context instance of [LocationRepository] */
        val notificationRepository: NotificationRepository by lazy { RepositoryContext.notificationRepository }

        /** Context instance of [LocationRepository] */
        val locationRepository: LocationRepository by lazy { RepositoryContext.locationRepository }

        /** Context instance of [SessionRepository] */
        val sessionRepository: SessionRepository by lazy { RepositoryContext.sessionRepository }

        /** Context instance of [TaskRepository] */
        val taskRepository: TaskRepository by lazy { RepositoryContext.taskRepository }

        /** Context instance of [UserRepository] */
        val userRepository: UserRepository by lazy { RepositoryContext.userRepository }

        /**
         * Builds the specified ViewModel
         *
         * @param T Type of the ViewModel to build
         * @param modelClass class of the ViewModel to build
         * @return instance of the ViewModel built
         */
        fun <T : ViewModel?> build(modelClass: Class<T>): T = modelClass.getConstructor(this::class.java).newInstance(this)

    }

}