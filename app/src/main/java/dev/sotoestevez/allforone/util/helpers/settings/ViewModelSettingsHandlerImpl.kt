package dev.sotoestevez.allforone.util.helpers.settings

import dev.sotoestevez.allforone.ui.viewmodel.WithSettings
import dev.sotoestevez.allforone.util.extensions.logDebug

/**
 * Default implementation of the [ViewModelSettingsHandler]
 *
 * @property model  model to manage the requests and operations
 */
class ViewModelSettingsHandlerImpl(private val model: WithSettings) : ViewModelSettingsHandler {

    private val sessionRepository = model.sessionRepository
    private val userRepository = model.userRepository

    override fun closeSession() {
        model.runRequest {
            sessionRepository.signOut(it)
            model.toLaunch()
        }
    }

}