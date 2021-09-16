package com.example.todoapp.util

import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.adapter.TasksAdapter
import com.example.todoapp.viewmodel.TaskViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.*

inline fun SearchView.onQueryTextChanged(crossinline listener:(String)->Unit)
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
                viewModel.deleteTask(adapter.getTask(position))
                val snack = Snackbar.make(
                    viewHolder.itemView,
                    "task got deleted",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("Undo") {
                        viewModel.insertTask(task)
                    }
                snack.show()
            }
        }
    }
}
