package dev.sotoestevez.allforone.ui.setup.fragments

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
import dev.sotoestevez.allforone.model.setup.SetUpViewModel
import dev.sotoestevez.allforone.ui.BaseExtendedFragment

/**
 * [Fragment] of SetUpActivity to select the [User.Role].
 */
class RoleSelectionFragment : BaseExtendedFragment() {

	private val binding
		get() = _binding!!
	private var _binding: FragmentRoleSelectionBinding? = null

	override val model: SetUpViewModel by activityViewModels()

	override fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
		_binding = FragmentRoleSelectionBinding.inflate(inflater, container, false)
		binding.model = model
		binding.lifecycleOwner = this
		return binding.root
	}

	override fun attachListeners() {
		super.attachListeners()
		binding.layButtonsRoleSelection.btnPrevious.setOnClickListener {
			findNavController().navigate(R.id.action_RoleSelectionFragment_to_NameSelectionFragment)
		}
		binding.layButtonsRoleSelection.btnNext.setOnClickListener {
			findNavController().navigate(R.id.action_RoleSelectionFragment_to_ContactFillFragment)
		}
	}

	override fun attachObservers() {
		super.attachObservers()
		model.selectedRole.observe(viewLifecycleOwner) { updateUi() }
	}

	override fun updateUi() {
		super.updateUi()
		binding.layButtonsRoleSelection.btnNext.isEnabled = model.user.value?.role != User.Role.BLANK
	}

}