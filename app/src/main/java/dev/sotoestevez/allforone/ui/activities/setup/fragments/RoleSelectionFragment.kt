package dev.sotoestevez.allforone.ui.activities.setup.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.databinding.FragmentRoleSelectionBinding
import dev.sotoestevez.allforone.ui.activities.setup.SetUpViewModel
import dev.sotoestevez.allforone.ui.components.fragments.BaseExtendedFragment

/** [Fragment] of SetUpActivity to select the [User.Role]. */
class RoleSelectionFragment : BaseExtendedFragment() {

    private val binding
        get() = _binding!!
    private var _binding: FragmentRoleSelectionBinding? = null

    override val model: SetUpViewModel by activityViewModels()

    @Suppress("KDocMissingDocumentation")   // override method
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.layButtonsRoleSelection.btnPositive.isEnabled = model.user.value?.role != User.Role.BLANK
    }

    override fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentRoleSelectionBinding.inflate(inflater, container, false)
        binding.model = model
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun attachListeners() {
        super.attachListeners()
        binding.layButtonsRoleSelection.run {
            btnNegative.setOnClickListener {
                findNavController().navigate(R.id.action_RoleSelectionFragment_to_NameSelectionFragment)
            }
            btnPositive.setOnClickListener {
                findNavController().navigate(R.id.action_RoleSelectionFragment_to_ContactFillFragment)
            }
        }
    }

    override fun attachObservers() {
        super.attachObservers()
        model.selectedRole.observe(viewLifecycleOwner) {
            if (it != User.Role.BLANK) {
                binding.layButtonsRoleSelection.btnPositive.isEnabled = true
            }
        }
    }

}