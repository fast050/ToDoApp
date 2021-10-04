package com.example.todoapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todoapp.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {



    fun getTask(searchQuery:String,orderBy:OrderBy,hide:Boolean) :Flow<List<Task>>
    {
       return when(orderBy)
        {
            OrderBy.OrderByDate->getTaskSortedByDate(searchQuery,hide)
            OrderBy.OrderByNote->getTaskSortedByNote(searchQuery,hide)
        }
    }
    @Query("Select * From task where(completion=0 or completion!=:hide) and note Like '%'||:searchQuery||'%' Order By importance DESC,createDate")
    fun getTaskSortedByDate(searchQuery:String,hide:Boolean) :Flow<List<Task>>

    @Query("Select * From task where(completion=0 or completion!=:hide) and note Like '%'||:searchQuery||'%' order by importance DESC,note")
    fun getTaskSortedByNote(searchQuery:String,hide:Boolean):Flow<List<Task>>

    @Insert
    suspend fun insertTasks(vararg tasks: Task)

    @Update
    suspend fun updateTasks(vararg tasks: Task)

    @Delete
    suspend fun deleteTasks(tasks: Task)

    @Query("Delete from task where completion = 1")
    suspend fun deleteCompleteTasks()


}