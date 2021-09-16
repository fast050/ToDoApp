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
        fun onClick(position: Int)
        fun onLongClick(position: Int)
        fun onClickCheck(position: Int,boolean: Boolean)
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
                    listener.onClickCheck(adapterPosition,binding.completeTask.isChecked)
                }
            }

            binding.root.apply {



                setOnClickListener{
                    if (adapterPosition!=RecyclerView.NO_POSITION) {
                        listener.onClick(adapterPosition)
                    }
                }

                setOnLongClickListener{

                    if (adapterPosition!=RecyclerView.NO_POSITION) {
                    listener.onLongClick(adapterPosition)
                    }
                    true
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

