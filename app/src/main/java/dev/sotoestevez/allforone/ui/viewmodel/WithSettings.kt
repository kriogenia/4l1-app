package dev.sotoestevez.allforone.ui.viewmodel

import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.repositories.UserRepository

/** ViewModel of an Activity featuring a notifications management */
interface WithSettings {

    /** Session repository used by the ViewModel */
    val sessionRepository: SessionRepository

    /** Session repository used by the ViewModel */
    val userRepository: UserRepository

    /** Request for ViewModel to run an asynchronous settings request */
    fun runRequest(request: suspend (String) -> Unit)

    /** Call to perform a navigation to LaunchActivity */
    fun toLaunch()

    /** Call to invoke the removal of the current bond */
    fun removeBond(callback: () -> Unit)

}