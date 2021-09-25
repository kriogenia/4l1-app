package dev.sotoestevez.allforone.ui.components.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.databinding.library.baseAdapters.BR

/**
 * RecyclerView able to work with DataBinding based on [https://github.com/kozmi55/Flexible-RecyclerView-with-Databinding]
 */
class BindableRecyclerViewAdapter : RecyclerView.Adapter<BindableRecyclerViewAdapter.BindableViewHolder>() {

	/** List of binded items to display in the recycler view */
	private var items: List<BindedItemView> = emptyList()
	private val viewTypeToLayoutId: MutableMap<Int, Int> = mutableMapOf()

	@Suppress("KDocMissingDocumentation")
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindableViewHolder {
		val binding: ViewDataBinding = DataBindingUtil.inflate(
			LayoutInflater.from(parent.context),
			viewTypeToLayoutId[viewType] ?: 0,
			parent,
			false)
		return BindableViewHolder(binding)
	}

	@Suppress("KDocMissingDocumentation")
	override fun getItemViewType(position: Int): Int {
		val item = items[position]
		if (!viewTypeToLayoutId.containsKey(item.viewType)) {
			viewTypeToLayoutId[item.viewType] = item.layoutId
		}
		return item.viewType
	}

	@Suppress("KDocMissingDocumentation")
	override fun getItemCount(): Int = items.size

	@Suppress("KDocMissingDocumentation")
	override fun onBindViewHolder(holder: BindableViewHolder, position: Int) {
		holder.bind(items[position])
	}

	/**
	 * Receives a new list of items and notifies the recycler view about the change
	 *
	 * @param items	new list of items
	 */
	fun updateItems(items: List<BindedItemView>?) {
		this.items = items ?: emptyList()
		notifyDataSetChanged()		// TODO give a look at this
	}

	/**
	 * Data bindable view holder
	 *
	 * @property binding
	 */
	inner class BindableViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

		/**
		 * Binds the item and the view holder
		 *
		 * @param item		Object to bind with the view holder
		 */
		fun bind(item: BindedItemView) {
			binding.setVariable(BR.item, item)
		}

	}

}