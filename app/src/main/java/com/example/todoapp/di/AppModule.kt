package com.example.todoapp.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.example.todoapp.db.DATABASE_NAME
import com.example.todoapp.db.TaskDao
import com.example.todoapp.db.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTaskDatabase(applicationContext: Application,
                            callback:TaskDatabase.TaskDatabaseCall  )
    : TaskDatabase = Room.databaseBuilder(
        applicationContext, TaskDatabase::class.java,
        DATABASE_NAME
    ).fallbackToDestructiveMigration().addCallback(callback).build()

    @Provides
    fun provideTaskDao(db: TaskDatabase): TaskDao = db.taskDao()

    @Provides
    @ApplicationScope
    fun provideCoroutineScope():CoroutineScope= CoroutineScope(SupervisorJob())


}
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope