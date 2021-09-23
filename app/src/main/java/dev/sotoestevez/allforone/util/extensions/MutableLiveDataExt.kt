package dev.sotoestevez.allforone.util.extensions

import androidx.lifecycle.MutableLiveData

/** Invokes the LiveData observers */
fun MutableLiveData<*>.invoke() = this.apply { value = value }

