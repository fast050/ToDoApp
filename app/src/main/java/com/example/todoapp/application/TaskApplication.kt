package com.example.todoapp.application

import android.app.Application
import com.example.todoapp.db.TaskDatabase
import com.example.todoapp.repository.TasksRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TaskApplication:Application()
{}