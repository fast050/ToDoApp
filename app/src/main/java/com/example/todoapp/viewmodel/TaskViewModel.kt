package com.example.todoapp.viewmodel

import androidx.lifecycle.*
import com.example.todoapp.db.OrderBy
import com.example.todoapp.db.PreferencesManager
import com.example.todoapp.model.Task
import com.example.todoapp.repository.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TasksRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {


    val searchQuery = MutableStateFlow("")
    private val preferenceFlow = preferencesManager.preferencesFlow


    val getTasks = combine(searchQuery, preferenceFlow) { searchQuery, PreferenceSaves ->
        Pair(searchQuery, PreferenceSaves)
    }
        .flatMapLatest { (search, PreferenceSaves) ->
            repository.getAllTasks(
                searchQuery = search,
                orderBy = PreferenceSaves.orderBy,
                hide = PreferenceSaves.hideTask
            )
        }.asLiveData()


    //SingleEvent handle
    private val events = Channel<SingleEvent>()

    val eventObserver = events.receiveAsFlow()

    fun onSwapDelete(task: Task) = viewModelScope.launch {
        deleteTask(task)
        events.send(SingleEvent.OnShowDeleteSnackBar(task))
    }

    fun onFragmentNavigateToNewTask() = viewModelScope.launch {
        events.send(SingleEvent.OnNavigationFragmentNewTask)
    }

    fun onFragmentNavigateToEditTask(task :Task) = viewModelScope.launch {
        events.send(SingleEvent.OnNavigationFragmentEditTask(task))
    }


    fun onOrderBySelected(orderBy: OrderBy) = viewModelScope.launch {
        preferencesManager.updateOrderBy(orderBy)
    }

    fun onHideItemClicked(condition: Boolean) = viewModelScope.launch {
        preferencesManager.updateHideTask(condition)
    }

    fun insertTask(tasks: Task) = viewModelScope.launch {
        repository.insertTasks(tasks)
    }

    private fun deleteTask(tasks: Task) = viewModelScope.launch {
        repository.deleteTasks(tasks)
    }

    private fun updateTask(tasks: Task) = viewModelScope.launch {
        repository.updateTasks(tasks)
    }

    fun deleteCompleteTasks() = viewModelScope.launch {
        repository.deleteCompleteTasks()
    }

    fun onCheckCompletion(task : Task, check :Boolean ) {
        val cloneTask=task.copy(completion = check)
        updateTask(cloneTask)
    }

    fun onShowSuccessMessage(message: String?) {
        viewModelScope.launch {
            if (message!= null && message.isNotBlank())
            events.send(SingleEvent.OnShowSuccessSnackBar(message))
        }
    }

    sealed class SingleEvent {
        object OnNavigationFragmentNewTask : SingleEvent()

        data class OnNavigationFragmentEditTask(val task: Task) : SingleEvent()

        data class OnShowDeleteSnackBar(val task: Task) : SingleEvent()

        data class OnShowSuccessSnackBar(val message:String):SingleEvent()

    }

}

