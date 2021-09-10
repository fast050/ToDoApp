package com.example.todoapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todoapp.db.TaskDao
import com.example.todoapp.db.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DBNAME = "task_database"

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): TaskDatabase {
        return Room.databaseBuilder(context.applicationContext
                                    , TaskDatabase::class.java
                                    , DBNAME).build()

    }

   @Provides
   @Singleton
   fun providesDao(db:TaskDatabase):TaskDao
   {
       return db.taskDao()
   }

}