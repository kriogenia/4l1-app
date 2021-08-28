package dev.sotoestevez.allforone.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.entities.SessionManager

/** Extension of the ViewModel with common declarations */
interface ExtendedViewModel {

	/**	Module to manage the tokens currently stored in memory */
	val sessionManager: SessionManager

	/** Live data with the current user info **/
	val user: LiveData<User>

	/** Live data holding the error to handle in the Activity */
	val error: LiveData<Throwable>

	/** Mutable live data to set the activity loading state from the model or the ui */
	val loading: MutableLiveData<Boolean>

}