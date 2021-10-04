package com.example.todoapp.model

import android.content.Context
import android.os.Parcelable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*


@Parcelize
@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val note: String,
    val importance: Boolean = false,
    val completion: Boolean = false,
    val createDate: String=  SimpleDateFormat.getDateTimeInstance().format(Calendar.getInstance().time)
): Parcelable {
}


//getCurrentDateTime().toString("MM dd HH:mm:ss")
/*
fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}*/
