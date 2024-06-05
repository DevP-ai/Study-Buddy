package com.developer.android.dev.technologia.androidapp.studybuddy.di

import android.app.Application
import androidx.room.Room
import com.developer.android.dev.technologia.androidapp.studybuddy.data.local.AppDatabase
import com.developer.android.dev.technologia.androidapp.studybuddy.data.local.SessionDao
import com.developer.android.dev.technologia.androidapp.studybuddy.data.local.SubjectDao
import com.developer.android.dev.technologia.androidapp.studybuddy.data.local.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "studybuddy.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideSubjectDao(database: AppDatabase): SubjectDao {
        return database.subjectDao()
    }

    @Singleton
    @Provides
    fun provideTaskDao(database: AppDatabase):TaskDao{
        return database.taskDao()
    }

    @Singleton
    @Provides
    fun provideSessionDao(database: AppDatabase):SessionDao{
        return database.sessionDao()
    }
}