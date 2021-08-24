package dev.sotoestevez.allforone.ui.setup.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.util.Strings
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.databinding.FragmentContactFillBinding
import dev.sotoestevez.allforone.model.blank.SetUpViewModel
import dev.sotoestevez.allforone.util.extensions.logDebug

/**
 * [Fragment] of SetUpActivity to set the remaining contact info of the user.
 */
class ContactFillFragment : Fragment() {

	private val binding
		get() = _binding!!
	private var _binding: FragmentContactFillBinding? = null

	private val model: SetUpViewModel by activityViewModels()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		//super.onCreateView(inflater, container, savedInstanceState)
		logDebug("onCreateView")
		_binding = FragmentContactFillBinding.inflate(inflater, container, false)
		return binding.root

	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		logDebug("onViewCreated")
		super.onViewCreated(view, savedInstanceState)
	}

	override fun onDestroyView() {
		logDebug("onDestroyView")
		super.onDestroyView()
		_binding = null
	}
}