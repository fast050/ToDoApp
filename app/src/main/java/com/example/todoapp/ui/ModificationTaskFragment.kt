package com.example.todoapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment

import android.view.View
import androidx.fragment.app.viewModels

import androidx.navigation.fragment.navArgs
import com.example.todoapp.R
import com.example.todoapp.application.TaskApplication
import com.example.todoapp.databinding.FragmentModificationTaskBinding
import com.example.todoapp.model.Task
import com.example.todoapp.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


@AndroidEntryPoint
class ModificationTaskFragment : Fragment(R.layout.fragment_modification_task) {

    private var _binding: FragmentModificationTaskBinding? = null
    private val binding get() = _binding!!
    private val args: ModificationTaskFragmentArgs by navArgs()
    private val viewModel:TaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentModificationTaskBinding.bind(view)

         val task = args.taskItem

        binding.apply {
            switchCompletion.isChecked = task.completion
            switchImportance.isChecked = task.importance
            editTextTextMultiLine.setText(task.note)
            createDateModfication.text = "Modify at " + task.createDate

            floatActionButtonUpdateTask.setOnClickListener{
                updateTask()
            }

        }

    }

    private fun updateTask() {
        binding.apply {
            val oldTask = args.taskItem

            val idTask = oldTask.id
            val important = switchImportance.isChecked
            val complete = switchCompletion.isChecked
            val note = editTextTextMultiLine.text.toString()

            val task = Task(
                id = idTask,
                importance = important,
                completion = complete, note = note
            )

            viewModel.updateTask(task)
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}

