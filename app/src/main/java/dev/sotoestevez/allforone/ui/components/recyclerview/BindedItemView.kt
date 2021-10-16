package dev.sotoestevez.allforone.ui.components.recyclerview

import androidx.annotation.LayoutRes

/** Wrapper of value objects to display in the [BindableRecyclerViewAdapter] */
interface BindedItemView {

	/** Identification of the view */
	val id: String

	/** Id of the layout associated with the object */
	@get:LayoutRes
	val layoutId: Int

	/** ViewType to work with the RecyclerView lifecycle */
	val viewType: Int

}