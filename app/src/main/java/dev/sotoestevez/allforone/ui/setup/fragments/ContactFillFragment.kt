package dev.sotoestevez.allforone.ui.setup.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.databinding.FragmentContactFillBinding
import dev.sotoestevez.allforone.model.setup.SetUpViewModel
import dev.sotoestevez.allforone.ui.BaseExtendedFragment

/**
 * [Fragment] of SetUpActivity to set the remaining contact info of the user.
 */
class ContactFillFragment : BaseExtendedFragment() {

	private val binding
		get() = _binding!!
	private var _binding: FragmentContactFillBinding? = null

	override val model: SetUpViewModel by activityViewModels()

	override fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
		_binding = FragmentContactFillBinding.inflate(inflater, container, false)
		binding.user = model.user.value
		return binding.root
	}

	override fun attachListeners() {
		binding.eTxtContactPhone.doAfterTextChanged { model.setMainPhoneNumber(it.toString()) }
		binding.eTxtContactPhoneAlt.doAfterTextChanged { model.setAltPhoneNumber(it.toString()) }
		binding.eTxtContactEmail.doAfterTextChanged { model.setEmail(it.toString()) }

		binding.eTxtContactAddressStreet.doAfterTextChanged { updateAddress() }
		binding.eTxtContactAddressExtended.doAfterTextChanged { updateAddress() }
		binding.eTxtContactAddressLocality.doAfterTextChanged { updateAddress() }
		binding.eTxtContactAddressRegion.doAfterTextChanged { updateAddress() }

		binding.layButtonsContactFill.btnPrevious.setOnClickListener {
			findNavController().navigate(R.id.action_ContactFillFragment_to_RoleSelectionFragment)
		}
		binding.layButtonsContactFill.btnNext.setOnClickListener {
			findNavController().navigate(R.id.action_ContactFillFragment_to_SetUpConfirmationFragment)
		}
	}

	private fun updateAddress() {
		model.setAddress(
			binding.eTxtContactAddressStreet.text.toString(),
			binding.eTxtContactAddressExtended.text.toString(),
			binding.eTxtContactAddressLocality.text.toString(),
			binding.eTxtContactAddressRegion.text.toString()
		)
	}

}