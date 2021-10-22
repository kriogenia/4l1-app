package dev.sotoestevez.allforone.ui.activities.setup.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.databinding.FragmentContactFillBinding
import dev.sotoestevez.allforone.ui.activities.setup.SetUpViewModel
import dev.sotoestevez.allforone.ui.components.fragments.BaseExtendedFragment

/**
 * [Fragment] of SetUpActivity to set the remaining contact info of the user.
 */
class ContactFillFragment : BaseExtendedFragment() {

    private val binding
        get() = _binding!!
    private var _binding: FragmentContactFillBinding? = null

    override val model: SetUpViewModel by activityViewModels()

    override fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentContactFillBinding.inflate(inflater, container, false).apply { user = model.user.value }
        return binding.root
    }

    override fun attachListeners() {
        super.attachListeners()
        binding.run {
            eTxtContactPhone.doAfterTextChanged { model.setMainPhoneNumber(it.toString()) }
            eTxtContactPhoneAlt.doAfterTextChanged { model.setAltPhoneNumber(it.toString()) }
            eTxtContactEmail.doAfterTextChanged { model.setEmail(it.toString()) }

            eTxtContactAddressStreet.doAfterTextChanged { updateAddress() }
            eTxtContactAddressExtended.doAfterTextChanged { updateAddress() }
            eTxtContactAddressLocality.doAfterTextChanged { updateAddress() }
            eTxtContactAddressRegion.doAfterTextChanged { updateAddress() }

            layButtonsContactFill.run {
                btnNegative.setOnClickListener {
                    findNavController().navigate(R.id.action_ContactFillFragment_to_RoleSelectionFragment)
                }
                btnPositive.setOnClickListener {
                    findNavController().navigate(R.id.action_ContactFillFragment_to_SetUpConfirmationFragment)
                }
            }
        }
    }

    private fun updateAddress() = binding.run {
        model.setAddress(
            eTxtContactAddressStreet.text.toString(),
            eTxtContactAddressExtended.text.toString(),
            eTxtContactAddressLocality.text.toString(),
            eTxtContactAddressRegion.text.toString()
        )
    }

}