package com.example.todoapp.viewmodel

import androidx.lifecycle.*
import com.example.todoapp.model.Task
import com.example.todoapp.repository.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.util.concurrent.locks.Condition
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val repository: TasksRepository) : ViewModel() {


    val searchQuery = MutableStateFlow("")
    val sortedBy = MutableStateFlow(OrderBy.OrderByNote)
    val hideCompleteTask = MutableStateFlow(false)


    val getTasks = combine(searchQuery, sortedBy, hideCompleteTask) { searchQuery, sortedBy, hide ->
        Triple(searchQuery, sortedBy, hide)
    }
        .flatMapLatest { (search, sort, hide) ->
            repository.getAllTasks(searchQuery = search, orderBy = sort, hide = hide)
        }.asLiveData()


    fun insertTask(tasks: Task) = viewModelScope.launch {
        repository.insertTasks(tasks)
    }

    fun deleteTask(tasks: Task) = viewModelScope.launch {
        repository.deleteTasks(tasks)
    }

    fun updateTask(tasks: Task) = viewModelScope.launch {
        repository.updateTasks(tasks)
    }

    fun deleteCompleteTasks() = viewModelScope.launch {
        repository.deleteCompleteTasks()
    }

}

enum class OrderBy { OrderByDate, OrderByNote }