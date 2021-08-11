package dev.sotoestevez.allforone.model.factories

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner

class LaunchViewModelFactory(
	val owner: AppCompatActivity
): AbstractSavedStateViewModelFactory(
	owner,
	if (owner.intent != null) owner.intent.extras else null) {

	override fun <T : ViewModel?> create(
		key: String,
		modelClass: Class<T>,
		handle: SavedStateHandle
	): T {
		val ssh = SavedStateHandle::class.java
		val sp = SharedPreferences::class.java
		val sharedPreferences = owner.getSharedPreferences("SESSION", Context.MODE_PRIVATE)
		Log.d("LaunchViewModelFactory", key)
		return modelClass.getConstructor(ssh, sp).newInstance(handle, sharedPreferences)
	}

}