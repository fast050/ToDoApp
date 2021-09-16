package com.example.todoapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todoapp.di.ApplicationScope
import com.example.todoapp.model.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

const val DATABASE_NAME = "task_database"

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao


    class TaskDatabaseCall @Inject constructor(
        //we make database here lazy it will not be created until we use it (in onCreate CallBack)
        private val database: Provider<TaskDatabase>,
        @ApplicationScope private val coroutineScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().taskDao()

            coroutineScope.launch {
                dao.insertTasks(Task(note = "call John "))
                dao.insertTasks(Task(note = "run across the city",importance = true))
                dao.insertTasks(Task(note = "build an app"))
                dao.insertTasks(Task(note = "visit a friend",importance = true))
                dao.insertTasks(Task(note = "study for exam ",completion = true))
                dao.insertTasks(Task(note = "play some game"))
                dao.insertTasks(Task(note = "watch a TV series",completion = true))
                dao.insertTasks(Task(note = "call John "))


            }

        }
    }

}