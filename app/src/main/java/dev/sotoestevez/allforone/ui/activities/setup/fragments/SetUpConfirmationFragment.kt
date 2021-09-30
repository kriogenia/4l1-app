package dev.sotoestevez.allforone.ui.activities.setup.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.databinding.FragmentSetUpConfirmationBinding
import dev.sotoestevez.allforone.ui.activities.setup.SetUpViewModel
import dev.sotoestevez.allforone.ui.components.fragments.BaseExtendedFragment


/**
 * Last [Fragment] of SetUpActivity, it shows all the info that the user entered
 * and asks for confirmation. Once the user confirms the data, it's send to the
 * API to update it and the users is moved to its correct MainActivity
 */
class SetUpConfirmationFragment : BaseExtendedFragment() {

	private val binding: FragmentSetUpConfirmationBinding
		get() = _binding!!
	private var _binding: FragmentSetUpConfirmationBinding? = null

	override val model: SetUpViewModel by activityViewModels()

	override fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
		_binding = FragmentSetUpConfirmationBinding.inflate(inflater, container, false).apply { model = model }
		return binding.root
	}

	override fun attachListeners() {
		super.attachListeners()
		binding.layButtonsSetUpConfirmation.run {
			btnNegative.setOnClickListener {
				findNavController().navigate(R.id.action_SetUpConfirmationFragment_to_ContactFillFragment)
			}
			btnPositive.setOnClickListener { model.sendUpdate() }
		}
	}

	override fun attachObservers() {
		super.attachObservers()
		model.loading.observe(this) { uiLoading(it) }
	}

	private fun uiLoading(loading: Boolean) {
		binding.run {
			loadBarSetUp.visibility = if (loading) View.VISIBLE else View.GONE
			layButtonsSetUpConfirmation.run {
				btnNegative.isEnabled = !loading
				btnPositive.isEnabled = !loading
			}
		}
	}

}