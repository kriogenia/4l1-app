package dev.sotoestevez.allforone.util.helpers.settings

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import dev.sotoestevez.allforone.BR
import dev.sotoestevez.allforone.ui.viewmodel.WithSettings
import dev.sotoestevez.allforone.util.extensions.logDebug
import dev.sotoestevez.allforone.vo.User
import java.lang.IllegalStateException

/**
 * Handler to perform the settings actions with the viewmodel coroutine and network logic
 */
class ViewModelSettingsHandler(
    private val model: WithSettings,
    currentUser: User
) : BaseObservable() {

    val isPatient: Boolean = currentUser.role == User.Role.PATIENT

    @get:Bindable
    var updating: Boolean = false

    val user: User = User(currentUser.id, currentUser.role, currentUser.displayName,
        currentUser.mainPhoneNumber, currentUser.altPhoneNumber, currentUser.address, currentUser.email)

    private val sessionRepository = model.sessionRepository

    var settingsDismisser: () -> Unit = { throw IllegalStateException("Settings dismisser not set") }

    fun updateMode(state: Boolean) {
        updating = state
        notifyPropertyChanged(BR.updating)
    }

    fun confirmUpdate() {
        logDebug("Confirming user data update")
        model.updateUser(user)
        updateMode(false)
    }

    fun closeSession() {
        model.runRequest {
            sessionRepository.signOut(it)
            model.toLaunch()
        }
    }

    fun removeBond() {
        model.runRequest {
            model.removeBond { settingsDismisser() }
        }
    }

}