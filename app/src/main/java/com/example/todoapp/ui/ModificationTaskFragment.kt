package com.example.todoapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment

import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController

import androidx.navigation.fragment.navArgs
import com.example.todoapp.R
import com.example.todoapp.application.TaskApplication
import com.example.todoapp.databinding.FragmentModificationTaskBinding
import com.example.todoapp.model.Task
import com.example.todoapp.util.fragmentResult_Bundle_pair_key
import com.example.todoapp.util.fragmentResult_Request_key
import com.example.todoapp.viewmodel.ModificationTaskViewModel
import com.example.todoapp.viewmodel.TaskViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


@AndroidEntryPoint
class ModificationTaskFragment : Fragment(R.layout.fragment_modification_task) {

    private var _binding: FragmentModificationTaskBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ModificationTaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentModificationTaskBinding.bind(view)

        binding.apply {
            switchCompletion.isChecked = viewModel.taskCompletion
            switchImportance.isChecked = viewModel.taskImportance
            editTextTextMultiLine.setText(viewModel.taskNote)
            createDateModfication.text = "Modify at " + viewModel.taskCreatedData


            switchCompletion.setOnCheckedChangeListener { _, b ->
                viewModel.taskCompletion = b
            }

            switchImportance.setOnCheckedChangeListener { _, b ->
                viewModel.taskImportance = b
            }

            editTextTextMultiLine.addTextChangedListener {
                viewModel.taskNote = it.toString()
            }


            floatActionButtonUpdateTask.setOnClickListener {
                viewModel.onUpdateTask()
            }

        }



        lifecycleScope.launchWhenStarted {

            viewModel.eventFlow.collect { event->
                when(event){
                    is ModificationTaskViewModel.SingleEvent.MessageToUser -> {
                       Snackbar.make(requireView(),event.message,Snackbar.LENGTH_LONG).show()

                    }
                    is ModificationTaskViewModel.SingleEvent.NavigateToMainFragment -> {

                        binding.editTextTextMultiLine.clearFocus()

                        parentFragmentManager.setFragmentResult(fragmentResult_Request_key, bundleOf(
                            fragmentResult_Bundle_pair_key to event.message))

                        findNavController().popBackStack()
                    }
                }

            }
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}

