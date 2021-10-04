package com.example.todoapp.util

import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.adapter.TasksAdapter
import com.example.todoapp.model.Task
import com.example.todoapp.ui.ModificationTaskFragment
import com.example.todoapp.viewmodel.TaskViewModel

 fun SearchView.onQueryTextChanged(listener:(String)->Unit)
{
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener
    {
        override fun onQueryTextSubmit(query: String?): Boolean {
         return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty())
            return true
        }

    })
}

const val fragmentResult_Request_key="fragmentResult_Request_key"
const val fragmentResult_Bundle_pair_key="fragmentResult_Bundle_pair_key"




fun adapterItemTouchHelper (adapter:TasksAdapter , viewModel:TaskViewModel) = object : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.RIGHT
) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        when (direction) {
            ItemTouchHelper.RIGHT -> {
                val task = adapter.getTask(position)
                viewModel.onSwapDelete(task = task)

            }
        }
    }
}
