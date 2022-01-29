package dev.sotoestevez.allforone.ui.components.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.databinding.FragmentSettingsBinding
import dev.sotoestevez.allforone.util.helpers.settings.ViewModelSettingsHandler
import dev.sotoestevez.allforone.util.helpers.settings.ViewModelSettingsHandlerImpl


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
        return binding.root
    }

    @Suppress("KDocMissingDocumentation")  // override method, it overrides the dialog theme
    override fun getTheme(): Int = R.style.AppTheme_Dialog_Full

    @Suppress("KDocMissingDocumentation")  // override method
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener { this.dismiss() }
    }

}