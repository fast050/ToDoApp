package com.example.todoapp.ui


import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.adapter.TasksAdapter
import com.example.todoapp.databinding.FragmentTaskListBinding
import com.example.todoapp.model.Task
import com.example.todoapp.util.adapterItemTouchHelper
import com.example.todoapp.util.onQueryTextChanged
import com.example.todoapp.viewmodel.OrderBy
import com.example.todoapp.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskListFragment : Fragment(R.layout.fragment_task_list) {

    private lateinit var adapter: TasksAdapter
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentTaskListBinding.bind(view)


        initRecyclerView()

        binding.addTask.setOnClickListener {

            val action = TaskListFragmentDirections.actionTaskListToNewTask()
            findNavController().navigate(action)
        }

        setHasOptionsMenu(true)

    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)

        val searchViewItem = menu.findItem(R.id.search_bar_menuItem)
        val searchView = searchViewItem.actionView as SearchView

       searchView.onQueryTextChanged{ query->
           viewModel.searchQuery.value=query
       }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.deleteComplete_menuItem -> viewModel.deleteCompleteTasks()

            R.id.sortByDate_menuItem -> {
              viewModel.sortedBy.value=OrderBy.OrderByDate
            }

            R.id.sortByNote_menuItem -> {
             viewModel.sortedBy.value=OrderBy.OrderByNote
            }

            R.id.hideComplete_menuItem -> {
                item.isChecked = !item.isChecked
                viewModel.hideCompleteTask.value=item.isChecked
            }
        }

        return super.onOptionsItemSelected(item)
    }


    private fun initRecyclerView() {
        var task: Task? = null

        adapter = TasksAdapter(object : TasksAdapter.OnClickModify {
            override fun onClick(position: Int) {
                viewModel.getTasks.observe(viewLifecycleOwner) {
                   // println("onClick view get the task data 5")
                    it?.let { task = it[position] }
                }

                if (task != null) {
                    val action = TaskListFragmentDirections.actionTaskListToModificationTask(task!!)
                    findNavController().navigate(action)
                   // println("task id and note is = ${task!!.id} , =${task!!.note} ")
                }
            }

            override fun onLongClick(position: Int) {
                // TODO("Not yet implemented")
            }

            override fun onClickCheck(position: Int, boolean: Boolean) {
                // TODO("Not yet implemented")
            }
        })

        val simpleCallback = adapterItemTouchHelper(adapter,viewModel)

        binding.apply {

            viewModel.getTasks.observe(viewLifecycleOwner)
            {
               // println("init adapter submit list 1")
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