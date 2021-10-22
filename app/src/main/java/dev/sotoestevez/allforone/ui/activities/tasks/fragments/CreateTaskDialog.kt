package dev.sotoestevez.allforone.ui.activities.tasks.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.databinding.FragmentCreateTaskBinding

/**
 * Dialog to manage the creation of a new task
 *
 * @property onCreate   callback to send the task input data
 */
class CreateTaskDialog(val onCreate: (String, String) -> Unit) : DialogFragment() {

    companion object {
        /** Tag of the fragment */
        const val TAG = "CreateTaskDialog"
    }

    private lateinit var binding: FragmentCreateTaskBinding

    /** User creator of the task */
    var submitter: String? = null

    @Suppress("KDocMissingDocumentation")   // override method
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateTaskBinding.inflate(inflater, container, false)
        submitter?.let { binding.lblTaskSubmitter.text = String.format(getString(R.string.created_by), submitter) }
        return binding.root
    }

    @Suppress("KDocMissingDocumentation")  // override method, it overrides the dialog theme
    override fun getTheme(): Int = R.style.AppTheme_FullDialog

    @Suppress("KDocMissingDocumentation")  // override method
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.layButtonsTask.apply {
            btnNegative.setOnClickListener { dismiss() }
            btnPositive.setOnClickListener {
                onCreate(binding.eTxtTaskTitle.text.toString(), binding.eTxtTaskDescription.text.toString())
                dismiss()
            }
        }
    }

}