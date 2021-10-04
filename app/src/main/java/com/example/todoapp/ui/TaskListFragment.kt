package com.example.todoapp.ui


import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.adapter.TasksAdapter
import com.example.todoapp.databinding.FragmentTaskListBinding
import com.example.todoapp.db.OrderBy
import com.example.todoapp.model.Task
import com.example.todoapp.util.adapterItemTouchHelper
import com.example.todoapp.util.fragmentResult_Bundle_pair_key
import com.example.todoapp.util.fragmentResult_Request_key
import com.example.todoapp.util.onQueryTextChanged
import com.example.todoapp.viewmodel.TaskViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

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


        parentFragmentManager.setFragmentResultListener(fragmentResult_Request_key,viewLifecycleOwner){
            _,bundle->
            val message = bundle.getString(fragmentResult_Bundle_pair_key,"")
            viewModel.onShowSuccessMessage(message)
        }


        //Events observer that happen single time
        lifecycleScope.launchWhenStarted {
            viewModel.eventObserver.collect { event ->
                when (event) {
                    TaskViewModel.SingleEvent.OnNavigationFragmentNewTask -> {
                        val action = TaskListFragmentDirections.actionTaskListToNewTask()
                        findNavController().navigate(action)
                    }
                    is TaskViewModel.SingleEvent.OnShowDeleteSnackBar -> {
                        Snackbar.make(requireView(), "Task Deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo") {
                                viewModel.insertTask(event.task)
                            }.show()

                    }
                    is TaskViewModel.SingleEvent.OnNavigationFragmentEditTask -> {
                        val action = TaskListFragmentDirections.actionTaskListToModificationTask(event.task)
                        findNavController().navigate(action)
                    }
                    is TaskViewModel.SingleEvent.OnShowSuccessSnackBar -> {
                        Snackbar.make(requireView(),event.message,Snackbar.LENGTH_SHORT).show()
                    }
                }

            }
        }


        binding.addTask.setOnClickListener {

            viewModel.onFragmentNavigateToNewTask()
        }

        setHasOptionsMenu(true)

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)

        val searchViewItem = menu.findItem(R.id.search_bar_menuItem)
        val searchView = searchViewItem.actionView as SearchView

        searchView.onQueryTextChanged { query ->
            viewModel.searchQuery.value = query
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.deleteComplete_menuItem -> viewModel.deleteCompleteTasks()

            R.id.sortByDate_menuItem -> {
                viewModel.onOrderBySelected(orderBy = OrderBy.OrderByDate)
            }

            R.id.sortByNote_menuItem -> {
                viewModel.onOrderBySelected(orderBy = OrderBy.OrderByNote)
            }

            R.id.hideComplete_menuItem -> {
                item.isChecked = !item.isChecked
                viewModel.onHideItemClicked(item.isChecked)
            }
        }

        return super.onOptionsItemSelected(item)
    }


    private fun initRecyclerView() {


        adapter = TasksAdapter(object : TasksAdapter.OnClickModify {
            override fun onClick(task: Task) {
               viewModel.onFragmentNavigateToEditTask(task)
            }


            override fun onClickCheck(task: Task, boolean: Boolean) {
                viewModel.onCheckCompletion(task,boolean)
            }
        })

        val simpleCallback = adapterItemTouchHelper(adapter, viewModel)

        binding.apply {

            viewModel.getTasks.observe(viewLifecycleOwner)
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