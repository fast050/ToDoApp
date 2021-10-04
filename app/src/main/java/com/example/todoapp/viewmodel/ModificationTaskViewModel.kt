package com.example.todoapp.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.Task
import com.example.todoapp.repository.TasksRepository
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ModificationTaskViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle, private val repository: TasksRepository
) : ViewModel() {

    val task = savedStateHandle.get<Task>("Task_item")

    var taskNote = savedStateHandle.get<String>("taskNote") ?: task?.note ?: Default.no_Task
        set(value) {
            field = value
            savedStateHandle.set("taskNote", value)
        }

    var taskImportance =
        savedStateHandle.get<Boolean>("taskImportance") ?: task?.importance ?: false
        set(value) {
            field = value
            savedStateHandle.set("taskImportance", value)
        }

    var taskCompletion =
        savedStateHandle.get<Boolean>("taskCompletion") ?: task?.completion ?: false
        set(value) {
            field = value
            savedStateHandle.set("taskCompletion", value)
        }

    val taskCreatedData: String =
        SimpleDateFormat.getDateTimeInstance().format(Calendar.getInstance().time)

    fun onUpdateTask() {

        val updatedTask =
            task?.copy(note = taskNote, importance = taskImportance, completion = taskCompletion)

        if (updatedTask == null) {
            viewModelScope.launch {
                eventChannel.send(SingleEvent.MessageToUser("Something Wrong Happen"))
            }
            return
        }

        if (updatedTask.note.isBlank()) {
            viewModelScope.launch {
                eventChannel.send(SingleEvent.MessageToUser("please add note!"))
            }
            return
        }

        updateTask(updatedTask)

    }


    private fun updateTask(task: Task) = viewModelScope.launch {
        repository.updateTasks(task)
        eventChannel.send(SingleEvent.NavigateToMainFragment("Updated Task Successfully"))
    }

    private fun insertTask(task: Task) = viewModelScope.launch {
        repository.insertTasks(task)
        eventChannel.send(SingleEvent.NavigateToMainFragment("Added Task Successfully") )
    }

    fun onCreateNewTask() {

        if (taskNote.isBlank() || taskNote == Default.no_Task) {
            viewModelScope.launch { eventChannel.send(SingleEvent.MessageToUser("please add note")) }
            return
        }

        val task = Task(note = taskNote, importance = taskImportance, completion = taskCompletion)

        insertTask(task)
    }


    private val eventChannel = Channel<SingleEvent>()

    val eventFlow = eventChannel.receiveAsFlow()


    sealed class SingleEvent {
        data class MessageToUser(val message: String) : SingleEvent()
        data class NavigateToMainFragment(val message: String) :SingleEvent()

    }

    object Default {
        const val no_Task = "com.example.todoapp.viewmodel_no_Task!"
    }


}