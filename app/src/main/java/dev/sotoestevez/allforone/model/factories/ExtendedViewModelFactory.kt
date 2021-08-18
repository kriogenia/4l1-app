package dev.sotoestevez.allforone.model.factories

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dev.sotoestevez.allforone.model.LaunchViewModel
import dev.sotoestevez.allforone.model.PrivateViewModel
import dev.sotoestevez.allforone.util.dispatcher.DefaultDispatcherProvider
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import java.lang.IllegalArgumentException

/**
 * Factory to generate ViewModels with SavedStateHandles and SharedPreferences
 *
 * @property activity of the ViewModel
 */
class ExtendedViewModelFactory(
	val activity: AppCompatActivity
): AbstractSavedStateViewModelFactory(
	activity,
	if (activity.intent != null) activity.intent.extras else null) {

	/**
	 * Factory method to build the desired ViewModel
	 *
	 * @param T type of the ViewModel
	 * @param key qualified name of the ViewModel
	 * @param modelClass class of the ViewModel
	 * @param handle handle of the SavedState to pass as parameter
	 * @return  expected ViewModel
	 */
	override fun <T : ViewModel?> create(
		key: String,
		modelClass: Class<T>,
		handle: SavedStateHandle
	): T {
		val ssh = SavedStateHandle::class.java
		val dp = DispatcherProvider::class.java
		return modelClass.getConstructor(ssh, dp).newInstance(handle, DefaultDispatcherProvider)
	/*	return when (key) {
			LaunchViewModel::class.qualifiedName -> modelClass.getConstructor(ssh, dp).newInstance(handle, DefaultDispatcherProvider)
			PrivateViewModel::class.qualifiedName -> modelClass.getConstructor(ssh, dp).newInstance(handle, DefaultDispatcherProvider)
			else -> throw IllegalArgumentException("The specified view model doesn't exist")
		}*/
	}

}