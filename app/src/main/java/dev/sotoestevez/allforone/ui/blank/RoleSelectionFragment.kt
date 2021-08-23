package dev.sotoestevez.allforone.ui.blank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.databinding.FragmentRoleSelectionBinding
import dev.sotoestevez.allforone.model.blank.SetUpViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RoleSelectionFragment : Fragment() {

	private val binding
		get() = _binding!!
	private var _binding: FragmentRoleSelectionBinding? = null

	private val model: SetUpViewModel by activityViewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {

		_binding = FragmentRoleSelectionBinding.inflate(inflater, container, false)
		return binding.root

	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.lblGreetingsName.text = getString(R.string.hello_name, model.user.value?.displayName)
		binding.btnPreviousRoleSelection.setOnClickListener {
			findNavController().navigate(R.id.action_RoleSelection_to_NameSelection)
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}