package dev.sotoestevez.allforone.model.interfaces

import androidx.lifecycle.MutableLiveData

/** ViewModel of on Activity featuring a profile card */
interface WithProfileCard {

	/** Defines if the featured profile card is expandable or not */
	val profileCardExpandable: Boolean

	/** Defines if the featured profile card is expandable or not */
	val profileCardWithBanner: Boolean

	/** LiveData managing the expansion state of the profile card */
	val profileCardExpanded: MutableLiveData<Boolean>

	/** Collapses expanded profile cards and expands collapse profile cards */
	fun onExpandButtonClick() {
		profileCardExpanded.value = !profileCardExpanded.value!!
	}

}