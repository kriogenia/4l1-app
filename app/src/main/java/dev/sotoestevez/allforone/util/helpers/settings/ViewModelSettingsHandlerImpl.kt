package dev.sotoestevez.allforone.util.helpers.settings

import androidx.databinding.BaseObservable
import dev.sotoestevez.allforone.ui.viewmodel.WithSettings
import java.lang.IllegalStateException

/**
 * Default implementation of the [ViewModelSettingsHandlerImpl]
 *
 * @property model  model to manage the requests and operations
 */
class ViewModelSettingsHandlerImpl(
    private val model: WithSettings,
    override val isPatient: Boolean
) : ViewModelSettingsHandler {

    private val sessionRepository = model.sessionRepository

    override var settingsDismisser: () -> Unit = { throw IllegalStateException("Settings dismisser not set") }

    override fun closeSession() {
        model.runRequest {
            sessionRepository.signOut(it)
            model.toLaunch()
        }
    }

    override fun removeBond() {
        model.runRequest {
            model.removeBond { settingsDismisser() }
        }
    }

}