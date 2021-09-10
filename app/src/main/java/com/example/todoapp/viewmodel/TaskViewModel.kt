package com.example.todoapp.viewmodel

import androidx.lifecycle.*
import com.example.todoapp.model.Task
import com.example.todoapp.repository.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.locks.Condition
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val repository: TasksRepository):ViewModel()
{

    val getAllTasks : LiveData<List<Task>> = repository.getAllTasks.asLiveData()

    fun insertTask(tasks: Task) =viewModelScope.launch{
       repository.insertTasks(tasks)
    }

     fun deleteTask(tasks: Task)=viewModelScope.launch {
        repository.deleteTasks(tasks)
    }

     fun updateTask(tasks: Task)=viewModelScope.launch {
        repository.updateTasks(tasks)
    }

    fun deleteAllCompleteTask(condition: Boolean) = viewModelScope.launch {
        repository.deleteAllCompleteTask(condition)
    }

    fun getHideCompleteTask(condition:Boolean): LiveData<List<Task>> = repository.getHideCompleteTasks(condition).asLiveData()

    fun getSortByTask(importance: String,number:Int) = repository.getSortByTask(importance,number).asLiveData()

}