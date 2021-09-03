package dev.sotoestevez.allforone.ui.bonds

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.data.User

class BondsAdapter : ListAdapter<User, BondsAdapter.BondViewHolder>(BondDiffCallback) {

	@Suppress("KDocMissingDocumentation")
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BondViewHolder {
		return BondViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.content_bond_item, parent, false))
	}

	@Suppress("KDocMissingDocumentation")
	override fun onBindViewHolder(holder: BondViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	inner class BondViewHolder(view: View) : RecyclerView.ViewHolder(view) {

		fun bind(user: User) {
			itemView.findViewById<TextView>(R.id.bondItemName).text = user.displayName
		}

	}

	/** Difference callback to use in the BondsAdapter */
	object BondDiffCallback: DiffUtil.ItemCallback<User>() {

		@Suppress("KDocMissingDocumentation")
		override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
			return oldItem == newItem
		}

		@Suppress("KDocMissingDocumentation")
		override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
			return oldItem.displayName == newItem.displayName
		}

	}

}
