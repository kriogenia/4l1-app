package dev.sotoestevez.allforone.ui.setup.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.util.Strings
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.databinding.FragmentNameSelectionBinding
import dev.sotoestevez.allforone.model.blank.SetUpViewModel
import dev.sotoestevez.allforone.util.extensions.logDebug

/**
 * [Fragment] of SetUpActivity to set the displayName of the user.
 */
class NameSelectionFragment : Fragment() {

	private val binding
		get() = _binding!!
	private var _binding: FragmentNameSelectionBinding? = null

	private val model: SetUpViewModel by activityViewModels()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {

		_binding = FragmentNameSelectionBinding.inflate(inflater, container, false)
		return binding.root

	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		updateUi()

		// Action listeners
		binding.btnNextNameSelection.setOnClickListener {
			if (model.setDisplayName(binding.txtNameSelection.text.toString())) {
				findNavController().navigate(R.id.action_NameSelectionFragment_to_RoleSelectionFragment)
			}
		}

	}

	override fun onDestroyView() {
		logDebug("onDestroyView")
		super.onDestroyView()
		_binding = null
	}

	private fun updateUi() {
		binding.txtNameSelection.setText(model.user.value?.displayName ?: "")
	}

}