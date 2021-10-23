package dev.sotoestevez.allforone.ui.components.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.sotoestevez.allforone.ui.view.SteppedCreation
import dev.sotoestevez.allforone.util.extensions.logDebug

/** Base [Fragment] to use as template for application fragments */
abstract class BaseExtendedFragment : Fragment(), ExtendedFragment, SteppedCreation {

    @Suppress("KDocMissingDocumentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        logDebug("Creating ${this::class.simpleName}")
        return bindLayout(inflater, container)
    }

    @Suppress("KDocMissingDocumentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        attachListeners()
        attachObservers()
    }

    override fun attachListeners() {}

    override fun attachObservers() {}

}