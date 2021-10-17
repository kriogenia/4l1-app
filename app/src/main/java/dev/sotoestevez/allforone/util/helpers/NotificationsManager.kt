package dev.sotoestevez.allforone.util.helpers

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import dev.sotoestevez.allforone.BR

class NotificationsManager: BaseObservable() {

    @get:Bindable
    val size: String
        get() = mSize.toString()
    var mSize: Int = 0

    fun increase() {
        ++mSize
        notifyPropertyChanged(BR.size)
    }

}