package dev.sotoestevez.allforone.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/** Base [Fragment] to use as template for application fragments */
abstract class MyFragment: Fragment() {

	@Suppress("KDocMissingDocumentation")
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		return bindLayout(inflater, container)
	}

	@Suppress("KDocMissingDocumentation")
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		attachListeners()
		attachObservers()
		updateUi()
	}

	/**
	 * Operation to bind the layout of the fragment
	 *
	 * @param inflater  Layout inflater
	 * @param container Fragment container
	 * @return  Binding root
	 */
	abstract fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View

	/** Operation to attach the fragment component listeners */
	protected open fun attachListeners() {}

	/** Operation to attach the model component observers */
	protected open fun attachObservers() {}

	/** Operation to update the UI */
	protected open fun updateUi() {}

}