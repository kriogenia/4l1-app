package dev.sotoestevez.allforone.util.helpers.settings

/** Handler to perform the settings actions with the viewmodel coroutine and network logic
 */
interface ViewModelSettingsHandler {

    val isPatient: Boolean

    var settingsDismisser: () -> Unit

    /**
     * Call to close the user session
     */
    fun closeSession()

    /**
     * Call to remove a user bond
     */
    fun removeBond()

}