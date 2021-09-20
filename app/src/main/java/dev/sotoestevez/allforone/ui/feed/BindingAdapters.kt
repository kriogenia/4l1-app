package dev.sotoestevez.allforone.ui.feed

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.sotoestevez.allforone.model.feed.MessageViewItem

@BindingAdapter("itemViewModels")
fun bindItemViewModels(recyclerView: RecyclerView, itemViewModels: List<MessageViewItem>?) {
	val adapter = getOrCreateAdapter(recyclerView)
	adapter.updateItems(itemViewModels)
}

private fun getOrCreateAdapter(recyclerView: RecyclerView): BindableRecyclerViewAdapter {
	return if (recyclerView.adapter != null && recyclerView.adapter is BindableRecyclerViewAdapter) {
		recyclerView.adapter as BindableRecyclerViewAdapter
	} else {
		val bindableRecyclerAdapter = BindableRecyclerViewAdapter()
		recyclerView.adapter = bindableRecyclerAdapter
		bindableRecyclerAdapter
	}
}