package com.example.todoapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.TaskItemBinding
import com.example.todoapp.model.Task

class TasksAdapter(private val listener: OnClickModify) :
    ListAdapter<Task ,TasksAdapter.TaskViewHolder >(Diff) {


    interface OnClickModify
    {
        fun onClick(task: Task)
        fun onClickCheck(task: Task,boolean: Boolean)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    fun getTask(position: Int): Task = getItem(position)

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) = holder.bind(getItem(position))


    inner class TaskViewHolder(private val binding: TaskItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {


            binding.completeTask.setOnClickListener {
                if (adapterPosition!=RecyclerView.NO_POSITION) {
                    listener.onClickCheck(getItem(adapterPosition),binding.completeTask.isChecked)
                }
            }

            binding.root.apply {



                setOnClickListener{
                    if (adapterPosition!=RecyclerView.NO_POSITION) {
                        val task=getItem(adapterPosition)
                        listener.onClick(task)
                    }
                }


            }

        }

        fun bind(task: Task) {
            binding.apply {
             completeTask.isChecked = task.completion
             noteTask.text=task.note
             noteTask.paint.isStrikeThruText = task.completion
             importanceTask.isVisible=task.importance
            }
        }
    }

    object Diff : DiffUtil.ItemCallback<Task>()
    {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem.id==newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem==newItem

    }
}

