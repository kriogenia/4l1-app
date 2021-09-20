package dev.sotoestevez.allforone.ui.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import dev.sotoestevez.allforone.model.feed.MessageViewItem
import androidx.databinding.library.baseAdapters.BR


class BindableRecyclerViewAdapter : RecyclerView.Adapter<BindableRecyclerViewAdapter.BindableViewHolder>() {

	var itemViewModels: List<MessageViewItem> = emptyList()
	private val viewTypeToLayoutId: MutableMap<Int, Int> = mutableMapOf()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindableViewHolder {
		val binding: ViewDataBinding = DataBindingUtil.inflate(
			LayoutInflater.from(parent.context),
			viewTypeToLayoutId[viewType] ?: 0,
			parent,
			false)
		return BindableViewHolder(binding)
	}

	override fun getItemViewType(position: Int): Int {
		val item = itemViewModels[position]
		if (!viewTypeToLayoutId.containsKey(item.viewType)) {
			viewTypeToLayoutId[item.viewType] = item.layoutId
		}
		return item.viewType
	}

	override fun getItemCount(): Int = itemViewModels.size

	override fun onBindViewHolder(holder: BindableViewHolder, position: Int) {
		holder.bind(itemViewModels[position])
	}

	fun updateItems(items: List<MessageViewItem>?) {
		itemViewModels = items ?: emptyList()
		notifyDataSetChanged()
	}

	inner class BindableViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

		fun bind(itemViewModel: MessageViewItem) {
			binding.setVariable(BR.messageViewItem, itemViewModel)
		}

	}
}