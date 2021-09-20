package dev.sotoestevez.allforone.ui.bonds

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.data.User

/** Adapter to manage the list of bonds of BondsActivity */
class BondsAdapter : ListAdapter<User, BondsAdapter.BondViewHolder>(BondDiffCallback) {

	@Suppress("KDocMissingDocumentation")
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BondViewHolder {
		return BondViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.content_bond_item, parent, false))
	}

	@Suppress("KDocMissingDocumentation")
	override fun onBindViewHolder(holder: BondViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	/**
	 * Each bond card in the bonds list
	 *
	 * @constructor
	 *
	 * @param view  View to inflate with the bond info
	 */
	inner class BondViewHolder(view: View) : RecyclerView.ViewHolder(view) {

		private val card = itemView.findViewById<LinearLayout>(R.id.cardBondItem)
		private val expandableView = itemView.findViewById<LinearLayout>(R.id.layBondExpandable)
		private val expandButton = itemView.findViewById<ImageButton>(R.id.btnExpandCard)

		/**
		 * Binding of the view with the user, particular onCreate
		 *
		 * @param user  User of the bond
		 */
		fun bind(user: User) {
			buildUI(user)
			setListeners()
			// TODO launch phone, email and Maps
		}

		private fun buildUI(user: User) {
			// TODO layout visibility
			itemView.findViewById<TextView>(R.id.txtBondName).text = user.displayName
			itemView.findViewById<TextView>(R.id.txtBondMainPhone).text = user.mainPhoneNumber
			itemView.findViewById<TextView>(R.id.txtBondAltPhone).text = user.altPhoneNumber
			itemView.findViewById<TextView>(R.id.txtBondStreet).text = user.address?.firstLine
			itemView.findViewById<TextView>(R.id.txtBondDoor).text = user.address?.secondLine
			itemView.findViewById<TextView>(R.id.txtBondLocality).text = user.address?.locality
			itemView.findViewById<TextView>(R.id.txtBondRegion).text = user.address?.region
			itemView.findViewById<TextView>(R.id.txtBondEmail).text = user.email
		}

		private fun setListeners(/*user: User*/) {
			expandButton.setOnClickListener { swapCardState() }
		}

		private fun swapCardState() {
			TransitionManager.beginDelayedTransition(card, AutoTransition())
			if (expandableView.visibility == View.GONE) {
				expandableView.visibility = View.VISIBLE
				expandButton.rotation = 180f    // TODO rotation animation
			} else {
				expandableView.visibility = View.GONE
				expandButton.rotation = 0f
			}
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
