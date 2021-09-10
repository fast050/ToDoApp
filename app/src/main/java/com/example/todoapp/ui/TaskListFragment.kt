package com.example.todoapp.ui

import android.graphics.Canvas
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.adapter.TasksAdapter
import com.example.todoapp.application.TaskApplication
import com.example.todoapp.databinding.FragmentTaskListBinding
import com.example.todoapp.model.Task
import com.example.todoapp.viewmodel.TaskViewModel
import com.example.todoapp.viewmodel.TaskViewModelFactory
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


class TaskListFragment : Fragment(R.layout.fragment_task_list) {

    lateinit var adapter: TasksAdapter
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!
    private var IsHide=false

    private val application by lazy { TaskApplication() }
    private val viewModel: TaskViewModel by viewModels {
        TaskViewModelFactory((requireActivity().application as TaskApplication).repository)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentTaskListBinding.bind(view)


        initRecyclerView()

        binding.floatingAdd.setOnClickListener() {

            val action = TaskListFragmentDirections.actionTaskListToNewTask()
            findNavController().navigate(action)
        }

        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)

        val searchViewItem = menu.findItem(R.id.search_bar_menuItem)
        val searchView = searchViewItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText != null && newText.isNotEmpty()) {

                }

                return true
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId)
        {
            R.id.deleteComplete_menuItem-> viewModel.deleteAllCompleteTask(condition = true)

            R.id.sortByDate_menuItem->{viewModel.getSortByTask("date",10).observe(viewLifecycleOwner){ adapter.submitList(it) }}

            R.id.sortByImportant_menuItem->{viewModel.getSortByTask("importance",10).observe(viewLifecycleOwner){ adapter.submitList(it) }}

            R.id.hideComplete_menuItem->{viewModel.getHideCompleteTask(IsHide).observe(viewLifecycleOwner){ adapter.submitList(it) };IsHide=!IsHide}
        }

        return super.onOptionsItemSelected(item)
    }


    private fun initRecyclerView() {
        var task: Task? = null

        adapter = TasksAdapter(object : TasksAdapter.OnClickModify {
            override fun onClick(position: Int) {
                viewModel.getAllTasks.observe(viewLifecycleOwner) {
                    it?.let { task = it[position] }
                }

                if (task != null) {
                    val action = TaskListFragmentDirections.actionTaskListToModificationTask(task!!)
                    findNavController().navigate(action)
                    println("task id and note is = ${task!!.id} , =${task!!.note} ")
                }
            }

            override fun onLongClick(position: Int) {
                // TODO("Not yet implemented")
            }
        })

        val simpleCallback = object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.RIGHT)
        {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder
                                 , target: RecyclerView.ViewHolder): Boolean = false


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                when (direction) {
                    ItemTouchHelper.RIGHT -> {
                       val task =adapter.getTask(position)
                        viewModel.deleteTask(adapter.getTask(position))
                        val snack = Snackbar.make(viewHolder.itemView,"task got deleted",Snackbar.LENGTH_LONG)
                                            .setAction("Undo"){
                                                viewModel.insertTask(task)
                                            }
                        snack.show()
                    }
                }
            }
        }


        binding.apply {

            viewModel.getAllTasks.observe(viewLifecycleOwner)
            {
                adapter.submitList(it)
            }
            recyclerViewTasks.adapter = adapter
            recyclerViewTasks.layoutManager = LinearLayoutManager(requireActivity())
            val itemTouchHelper = ItemTouchHelper(simpleCallback)
            itemTouchHelper.attachToRecyclerView(recyclerViewTasks)

        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}