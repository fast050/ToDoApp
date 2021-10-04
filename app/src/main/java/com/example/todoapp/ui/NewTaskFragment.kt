package com.example.todoapp.ui


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentNewTaskBinding
import com.example.todoapp.model.Task
import com.example.todoapp.util.fragmentResult_Bundle_pair_key
import com.example.todoapp.util.fragmentResult_Request_key
import com.example.todoapp.viewmodel.ModificationTaskViewModel
import com.example.todoapp.viewmodel.TaskViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class NewTaskFragment : Fragment(R.layout.fragment_new_task) {

    private var _binding: FragmentNewTaskBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ModificationTaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentNewTaskBinding.bind(view)


        binding.apply {

            newNote.addTextChangedListener {
              viewModel.taskNote=it.toString()
             }

            importantSwtich.setOnCheckedChangeListener { _, b ->
                viewModel.taskImportance=b

            }

            completeSwtich.setOnCheckedChangeListener { _, b ->
                viewModel.taskCompletion=b
            }

            val data = SimpleDateFormat.getDateTimeInstance().format(Calendar.getInstance().time)
            binding.createdNewTaskDate.text="created at ${data}"



            floatingActionButtonSaveTask.setOnClickListener{

                viewModel.onCreateNewTask()
            }

           lifecycleScope.launchWhenStarted {

               viewModel.eventFlow.collect { event->
                  when(event)
                  {
                      is ModificationTaskViewModel.SingleEvent.MessageToUser -> {
                          Snackbar.make(requireView(),event.message,Snackbar.LENGTH_SHORT).show()
                      }
                      is ModificationTaskViewModel.SingleEvent.NavigateToMainFragment -> {

                          newNote.clearFocus()

                          parentFragmentManager.setFragmentResult(fragmentResult_Request_key,
                              bundleOf(fragmentResult_Bundle_pair_key to event.message))

                          findNavController().popBackStack()
                       }
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