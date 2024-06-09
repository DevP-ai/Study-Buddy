package com.developer.android.dev.technologia.androidapp.studybuddy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Session
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Subject
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Task

@Database(
    entities = [Subject::class,Session::class, Task::class],
    version = 1, exportSchema = false
)
@TypeConverters(ColorListConverter::class)
abstract class AppDatabase:RoomDatabase(){

    abstract fun subjectDao():SubjectDao

    abstract  fun sessionDao():SessionDao

    abstract fun taskDao():TaskDao

}