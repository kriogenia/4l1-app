package dev.sotoestevez.allforone.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class MyFragment: Fragment() {

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		return bindLayout(inflater, container)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		attachListeners()
		attachObservers()
		updateUi()
	}

	abstract fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View
	protected open fun attachListeners() {}
	protected open fun attachObservers() {}
	protected open fun updateUi() {}
}