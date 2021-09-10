package com.example.todoapp.repository

import com.example.todoapp.db.TaskDao
import com.example.todoapp.model.Task
import kotlinx.coroutines.flow.Flow


class TasksRepository(private val taskDao: TaskDao) {

    val getAllTasks: Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun insertTasks(vararg tasks: Task) {
        taskDao.insertTasks(*tasks)
    }

    suspend fun deleteTasks(tasks: Task) {
        taskDao.deleteTasks(tasks)
    }

    suspend fun updateTasks(vararg tasks: Task) {
        taskDao.updateTasks(*tasks)
    }

    suspend fun deleteAllCompleteTask(condition:Boolean){
        taskDao.deleteCompleteTasks()
    }

    fun getHideCompleteTasks(condition: Boolean): Flow<List<Task>> = taskDao.hideCompleteTasks(condition)

    fun getSortByTask(orderBy : String,numberOfTask :Int):Flow<List<Task>>  =taskDao.getAllTop(orderBy,numberOfTask)


}