package dev.sotoestevez.allforone.ui.viewmodel

import androidx.lifecycle.MutableLiveData

/** ViewModel of an Activity featuring a profile card */
interface WithProfileCard {

    /** Defines if the featured profile card is expandable or not */
    val profileCardExpandable: Boolean

    /** LiveData managing the expansion state of the profile card */
    val profileCardExpanded: MutableLiveData<Boolean>

    /** Collapses expanded profile cards and expands collapse profile cards */
    fun onExpandButtonClick() {
        profileCardExpanded.value = !profileCardExpanded.value!!
    }

}