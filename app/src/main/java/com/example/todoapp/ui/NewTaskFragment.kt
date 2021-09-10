package com.example.todoapp.ui


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentNewTaskBinding
import com.example.todoapp.model.Task
import com.example.todoapp.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class NewTaskFragment : Fragment(R.layout.fragment_new_task) {

    private var _binding: FragmentNewTaskBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentNewTaskBinding.bind(view)


        val data = SimpleDateFormat.getDateTimeInstance().format(Calendar.getInstance().time)
        binding.createdNewTaskDate.text="created at $data"

        binding.floatingActionButtonSaveTask.setOnClickListener{

            addTaskToDatabase()
        }

    }

    private fun addTaskToDatabase()
    {
        binding.apply {


            val note: String = newNote.text.toString()
            val important:Boolean = importantSwtich.isChecked
            val complete:Boolean =completeSwtich.isChecked

            val newTask: Task?
            if (note.isNotEmpty())
            {
                newTask=Task(note = note,importance = important,completion = complete)
                viewModel.insertTask(tasks = newTask)

            }
            else
                Toast.makeText(requireActivity().applicationContext,"Add Task please",Toast.LENGTH_LONG).show()


        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}