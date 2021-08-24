package dev.sotoestevez.allforone.ui.setup.fragments

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.databinding.FragmentRoleSelectionBinding
import dev.sotoestevez.allforone.model.blank.SetUpViewModel
import dev.sotoestevez.allforone.ui.MyFragment
import dev.sotoestevez.allforone.util.extensions.logDebug

/**
 * [Fragment] of SetUpActivity to select the [User.Role].
 */
class RoleSelectionFragment : MyFragment() {

	private val binding
		get() = _binding!!
	private var _binding: FragmentRoleSelectionBinding? = null

	private val model: SetUpViewModel by activityViewModels()

	override fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
		_binding = FragmentRoleSelectionBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun attachListeners() {
		binding.btnPreviousRoleSelection.setOnClickListener {
			findNavController().navigate(R.id.action_RoleSelectionFragment_to_NameSelectionFragment)
		}
		binding.btnNextRoleSelection.setOnClickListener {
			findNavController().navigate(R.id.action_RoleSelectionFragment_to_ContactFillFragment)
		}
		binding.cardPatient.setOnClickListener {
			model.setRole(User.Role.PATIENT)
		}
		binding.cardKeeper.setOnClickListener {
			model.setRole(User.Role.KEEPER)
		}
	}

	override fun attachObservers() {
		model.user.observe(viewLifecycleOwner) {
			updateUi()
		}
	}

	override fun updateUi() {
		binding.lblGreetingsName.text = getString(R.string.hello_name, model.user.value?.displayName)
		binding.btnNextRoleSelection.isEnabled = model.user.value?.role != User.Role.BLANK
		if (model.user.value?.role == User.Role.PATIENT) {
			binding.cardPatient.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.cardview_dark_background))
			binding.cardKeeper.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.cardview_light_background))
		}
		if (model.user.value?.role == User.Role.KEEPER) {
			binding.cardKeeper.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.cardview_dark_background))
			binding.cardPatient.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.cardview_light_background))
		}
	}

}