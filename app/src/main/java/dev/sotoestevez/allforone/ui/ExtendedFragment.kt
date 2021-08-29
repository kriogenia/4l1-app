package dev.sotoestevez.allforone.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/** Common interface to all the fragments of the application */
interface ExtendedFragment: WithModel {

	/**
	 * Operation to bind the layout of the fragment
	 *
	 * @param inflater  Layout inflater
	 * @param container Fragment container
	 * @return  Binding root
	 */
	fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View

	/** Operation to update the UI */
	fun updateUi()

}