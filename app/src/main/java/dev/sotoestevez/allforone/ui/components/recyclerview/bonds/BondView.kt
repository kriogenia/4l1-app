package dev.sotoestevez.allforone.ui.components.recyclerview.bonds

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import dev.sotoestevez.allforone.BR
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.ui.components.recyclerview.BindedItemView
import dev.sotoestevez.allforone.vo.User
import java.lang.IllegalStateException

class BondView(
    val data: User,
    private val longPressCallback: (BondView) -> Unit
    ) : BaseObservable(), BindedItemView {

    override val id: String = data.displayName ?: throw IllegalStateException("Received bond without display name")

    override val layoutId: Int = R.layout.content_bond_item

    override val viewType: Int = 0

    /** Bindable completion state of the task */
    @get:Bindable
    val expanded: Boolean
        get() = mExpanded
    var mExpanded: Boolean = false

    fun swapState() {
        mExpanded = !mExpanded
        notifyPropertyChanged(BR.expanded)
    }

    /** On long press action listener */
    fun onLongPress() = true.also { longPressCallback(this) }

}