package com.example.todoapp.repository

import com.example.todoapp.db.TaskDao
import com.example.todoapp.model.Task
import com.example.todoapp.viewmodel.OrderBy
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TasksRepository @Inject constructor(private val taskDao: TaskDao) {

    fun getAllTasks(searchQuery: String, orderBy: OrderBy, hide: Boolean): Flow<List<Task>> =
        taskDao.getTask(
            searchQuery = searchQuery,
            orderBy = orderBy,
            hide = hide
        )

    suspend fun insertTasks(vararg tasks: Task) {
        taskDao.insertTasks(*tasks)
    }

    suspend fun deleteTasks(tasks: Task) {
        taskDao.deleteTasks(tasks)
    }

    suspend fun updateTasks(vararg tasks: Task) {
        taskDao.updateTasks(*tasks)
    }

    suspend fun deleteCompleteTasks(){
        taskDao.deleteCompleteTasks()
    }


}