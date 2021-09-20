package dev.sotoestevez.allforone.ui.components.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView list binder
 *
 * @param T	Object to contain and display in the RecyclerView
 * @param recyclerView	RecyclerView to display the items
 * @param heldItems	List of items to display
 */
@BindingAdapter("heldItems")
fun <T> bindItemViewModels(recyclerView: RecyclerView, heldItems: List<BindedItemView>?) {
	val adapter = getOrCreateAdapter<T>(recyclerView)
	adapter.updateItems(heldItems)
}

private fun <T> getOrCreateAdapter(recyclerView: RecyclerView): BindableRecyclerViewAdapter {
	return if (recyclerView.adapter != null && recyclerView.adapter is BindableRecyclerViewAdapter) {
		recyclerView.adapter as BindableRecyclerViewAdapter
	} else {
		val bindableRecyclerAdapter = BindableRecyclerViewAdapter()
		recyclerView.adapter = bindableRecyclerAdapter
		bindableRecyclerAdapter
	}
}