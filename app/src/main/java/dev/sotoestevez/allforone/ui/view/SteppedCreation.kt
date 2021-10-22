package dev.sotoestevez.allforone.ui.view

/** Common operations to application UI modules to organize the component creation */
interface SteppedCreation {

    /** Operation to attach the listeners to the UI components */
    fun attachListeners()

    /** Operation to attach the observers to the view model live data */
    fun attachObservers()

}