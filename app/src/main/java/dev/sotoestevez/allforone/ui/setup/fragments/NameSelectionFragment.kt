package dev.sotoestevez.allforone.ui.setup.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.util.Strings
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.databinding.FragmentNameSelectionBinding
import dev.sotoestevez.allforone.model.setup.SetUpViewModel
import dev.sotoestevez.allforone.ui.MyFragment

/**
 * [Fragment] of SetUpActivity to set the displayName of the user.
 */
class NameSelectionFragment : MyFragment() {

	private val binding: FragmentNameSelectionBinding
		get() = _binding!!
	private var _binding: FragmentNameSelectionBinding? = null

	private val model: SetUpViewModel by activityViewModels()

	override fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
		_binding = FragmentNameSelectionBinding.inflate(inflater, container, false)
		binding.name = model.user.value?.displayName
		return binding.root
	}

	override fun attachListeners() {
		binding.txtNameSelection.doAfterTextChanged { model.setDisplayName(it.toString()) }
		binding.btnNextNameSelection.setOnClickListener {
			findNavController().navigate(R.id.action_NameSelectionFragment_to_RoleSelectionFragment)
		}
	}

	override fun attachObservers() {
		model.user.observe(viewLifecycleOwner) { updateUi() }
	}

	override fun updateUi() {
		binding.btnNextNameSelection.isEnabled = !Strings.isEmptyOrWhitespace(model.user.value?.displayName)
	}

}