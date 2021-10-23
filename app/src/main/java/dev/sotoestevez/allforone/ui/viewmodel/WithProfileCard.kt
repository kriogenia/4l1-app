package dev.sotoestevez.allforone.ui.viewmodel

import androidx.lifecycle.MutableLiveData

/** ViewModel of an Activity featuring a profile card */
interface WithProfileCard {

    /** LiveData managing the reverse state of the profile card */
    val profileCardReversed: MutableLiveData<Boolean>

    /** Collapses expanded profile cards and expands collapse profile cards */
    fun onFlipButtonClick() {
        profileCardReversed.value = !profileCardReversed.value!!
    }

}