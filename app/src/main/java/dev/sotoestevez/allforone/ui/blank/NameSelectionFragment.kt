package dev.sotoestevez.allforone.ui.blank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.util.Strings
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.databinding.FragmentNameSelectionBinding
import dev.sotoestevez.allforone.model.blank.SetUpViewModel

/**
 * [Fragment] of [SetUpActivity] to set the displayName of the user.
 */
class NameSelectionFragment : Fragment() {

	private val binding
		get() = _binding!!
	private var _binding: FragmentNameSelectionBinding? = null

	private val model: SetUpViewModel by activityViewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {

		_binding = FragmentNameSelectionBinding.inflate(inflater, container, false)
		return binding.root

	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.btnNextNameSelection.setOnClickListener {
			model.setDisplayName(binding.txtNameSelection.text.toString())
		}

		model.user.observe(viewLifecycleOwner) {
			if (!Strings.isEmptyOrWhitespace(it.displayName))
				findNavController().navigate(R.id.action_NameSelection_to_RoleSelection)
		}

	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}