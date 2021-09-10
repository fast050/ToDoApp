package com.example.todoapp.application

import android.app.Application
import com.example.todoapp.db.TaskDatabase
import com.example.todoapp.repository.TasksRepository
import dagger.hilt.android.HiltAndroidApp


class TaskApplication:Application()
{

    private val database by lazy {TaskDatabase.getDatabase(this)}

    val repository by lazy { TasksRepository(taskDao =database.taskDao() ) }

}