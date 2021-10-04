package com.example.todoapp.db

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.locks.Condition
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.coroutineContext

//create a datastore by delegation
private val Context.dataStore by preferencesDataStore("settings")

data class PreferenceSaves(val hideTask: Boolean, val orderBy: OrderBy)

enum class OrderBy { OrderByDate, OrderByNote }

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext appContext: Context) {


    private val settingsDataStore = appContext.dataStore


    val preferencesFlow = settingsDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                //get the default value of preference if there error from IO
                emit(emptyPreferences())
            }
        }
        .map { preference ->
            val hideValue = preference[PreferenceKey.HIDE_TASK_KEY] ?: false
            val orderByValue = OrderBy.valueOf(
                preference[PreferenceKey.SORTED_ORDER] ?: OrderBy.OrderByDate.name
            )

            PreferenceSaves(hideTask = hideValue,orderBy = orderByValue)

        }



    suspend fun updateOrderBy(orderBy: OrderBy)
    {
        settingsDataStore.edit {
                orderByPreference->
            orderByPreference[PreferenceKey.SORTED_ORDER] = orderBy.name
        }
    }

    suspend fun updateHideTask(hideTask: Boolean)
    {
        settingsDataStore.edit {
                hidePreference->
            hidePreference[PreferenceKey.HIDE_TASK_KEY]= hideTask
        }
    }
    private object PreferenceKey {
        val HIDE_TASK_KEY = booleanPreferencesKey("hide_task")
        val SORTED_ORDER = stringPreferencesKey("sorted_order")
    }

}