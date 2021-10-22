package dev.sotoestevez.allforone.ui.view

import dev.sotoestevez.allforone.ui.viewmodel.ExtendedViewModel

/** Objects using an ExtendedViewModel */
interface WithModel {

    /** Model to handle the logic of the UI component */
    val model: ExtendedViewModel

}