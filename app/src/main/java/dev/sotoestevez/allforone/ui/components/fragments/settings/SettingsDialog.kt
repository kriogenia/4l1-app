package dev.sotoestevez.allforone.ui.components.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.databinding.FragmentSettingsBinding
import dev.sotoestevez.allforone.util.helpers.settings.ViewModelSettingsHandler
import dev.sotoestevez.allforone.vo.Address
import dev.sotoestevez.allforone.vo.User


/**
 * Dialog to with the application settings
 */
class SettingsDialog(private val handler: ViewModelSettingsHandler) : DialogFragment() {

    companion object {
        /** Tag of the fragment */
        const val TAG = "SettingsDialog"
    }

    private lateinit var binding: FragmentSettingsBinding

    @Suppress("KDocMissingDocumentation")   // override method
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        handler.settingsDismisser = { dismiss() }
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding.handler = handler
        binding.updating = handler.updating
        return binding.root
    }

    @Suppress("KDocMissingDocumentation")  // override method, it overrides the dialog theme
    override fun getTheme(): Int = R.style.AppTheme_Dialog_Full

    @Suppress("KDocMissingDocumentation")  // override method
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            btnBack.setOnClickListener { dismiss() }
            eTxtDisplayName.doAfterTextChanged { handler?.user?.displayName = it.toString() }
            eTxtContactPhone.doAfterTextChanged { handler?.user?.mainPhoneNumber = it.toString() }
            eTxtContactPhoneAlt.doAfterTextChanged { handler?.user?.altPhoneNumber = it.toString() }
            eTxtContactEmail.doAfterTextChanged { handler?.user?.email = it.toString() }

            eTxtContactAddressStreet.doAfterTextChanged { updateAddress(handler?.user!!) }
            eTxtContactAddressExtended.doAfterTextChanged { updateAddress(handler?.user!!) }
            eTxtContactAddressLocality.doAfterTextChanged { updateAddress(handler?.user!!) }
            eTxtContactAddressRegion.doAfterTextChanged { updateAddress(handler?.user!!) }
        }
    }

    private fun updateAddress(user: User) = binding.run {
        user.address = Address(
            eTxtContactAddressStreet.text.toString(),
            eTxtContactAddressExtended.text.toString(),
            eTxtContactAddressLocality.text.toString(),
            eTxtContactAddressRegion.text.toString()
        )
    }

}