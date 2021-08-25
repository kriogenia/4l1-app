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
import dev.sotoestevez.allforone.ui.MyFragment
import dev.sotoestevez.allforone.util.extensions.logDebug

/**
 * [Fragment] of SetUpActivity to set the remaining contact info of the user.
 */
class ContactFillFragment : MyFragment() {

	private val binding
		get() = _binding!!
	private var _binding: FragmentContactFillBinding? = null

	private val model: SetUpViewModel by activityViewModels()

	override fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
		_binding = FragmentContactFillBinding.inflate(inflater, container, false)
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

		binding.btnPreviousContactFill.setOnClickListener {
			findNavController().navigate(R.id.action_ContactFillFragment_to_RoleSelectionFragment)
		}
		binding.btnNextContactFill.setOnClickListener {
			findNavController().navigate(R.id.action_ContactFillFragment_to_SetUpConfirmationFragment)
		}
	}

	override fun updateUi() {
		binding.eTxtContactPhone.setText(model.user.value?.mainPhoneNumber ?: "")
		binding.eTxtContactPhoneAlt.setText(model.user.value?.altPhoneNumber ?: "")
		binding.eTxtContactEmail.setText(model.user.value?.email ?: "")

		val address = model.user.value?.address ?: return
		binding.eTxtContactAddressStreet.setText(address.getAddressLine(0))
		binding.eTxtContactAddressExtended.setText(address.getAddressLine(1))
		binding.eTxtContactAddressLocality.setText(address.locality)
		binding.eTxtContactAddressRegion.setText(address.adminArea)
	}

	private fun updateAddress() {
		model.setAddress(
			binding.eTxtContactAddressStreet.text.toString(),
			binding.eTxtContactAddressExtended.text.toString(),
			binding.eTxtContactAddressLocality.text.toString(),
			binding.eTxtContactAddressRegion.text.toString(),
			resources.configuration.locales.get(0)
		)
	}

}