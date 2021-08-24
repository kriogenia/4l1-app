package dev.sotoestevez.allforone.ui.setup.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.databinding.FragmentRoleSelectionBinding
import dev.sotoestevez.allforone.model.blank.SetUpViewModel
import dev.sotoestevez.allforone.util.extensions.logDebug

/**
 * [Fragment] of [SetUpActivity] to select the [User.Role].
 */
class RoleSelectionFragment : Fragment() {

	private val binding
		get() = _binding!!
	private var _binding: FragmentRoleSelectionBinding? = null

	private val model: SetUpViewModel by activityViewModels()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		logDebug("onCreateView")
		_binding = FragmentRoleSelectionBinding.inflate(inflater, container, false)
		return binding.root

	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		logDebug("onViewCreated")
		// Init
		binding.lblGreetingsName.text = getString(R.string.hello_name, model.user.value?.displayName)
		// Action listeners
		binding.btnPreviousRoleSelection.setOnClickListener {
			findNavController().navigate(R.id.action_RoleSelectionFragment_to_NameSelectionFragment)
		}
		binding.btnNextRoleSelection.setOnClickListener {
			findNavController().navigate(R.id.action_RoleSelectionFragment_to_ContactFillFragment)
		}
		binding.cardPatient.setOnClickListener {
			model.setRole(User.Role.PATIENT)
			binding.cardPatient.isSelected = true
		}
		binding.cardKeeper.setOnClickListener {
			model.setRole(User.Role.KEEPER)
			binding.cardKeeper.isSelected = true
		}
		// Observers
		model.user.observe(viewLifecycleOwner) {
			binding.btnNextRoleSelection.isEnabled = model.user.value?.role != User.Role.BLANK
		}
	}

	override fun onDestroyView() {
		logDebug("onDestroyView")
		super.onDestroyView()
		_binding = null
	}
}