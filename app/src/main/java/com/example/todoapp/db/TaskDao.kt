package com.example.todoapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todoapp.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("Select * From task")
    fun getAllTasks() : Flow<List<Task>>

    @Insert
    suspend fun insertTasks(vararg tasks: Task)

    @Update
    suspend fun updateTasks(vararg tasks: Task)

    @Delete
    suspend fun deleteTasks(tasks: Task)

    @Query("Select * From task Where case :condition when 1 then Not completion when 0 then note not null end")
    fun hideCompleteTasks(condition:Boolean):Flow<List<Task>>

    @Query("Delete From task Where completion")
    suspend fun deleteCompleteTasks()

    @Query("Select * From task ORDER BY CASE :order WHEN 'importance' THEN importance WHEN 'date' THEN note END ASC limit :numberOfCoins")
    fun getAllTop(order: String, numberOfCoins: Int): Flow<List<Task>>

    @Query("Delete From task Where completion")
    suspend fun sortByDate()

    @Query("Select * From task Where note Like :searchQuery")
    fun searchTask(searchQuery:String):Flow<List<Task>>

}