package com.developer.android.dev.technologia.androidapp.studybuddy.di

import com.developer.android.dev.technologia.androidapp.studybuddy.data.repository.SessionRepositoryImp
import com.developer.android.dev.technologia.androidapp.studybuddy.data.repository.SubjectRepositoryImp
import com.developer.android.dev.technologia.androidapp.studybuddy.data.repository.TaskRepositoryImp
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.repository.SessionRepository
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.repository.SubjectRepository
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Provides
    abstract fun bindSubjectRepository(subjectImpl:SubjectRepositoryImp):SubjectRepository


    @Singleton
    @Provides
    abstract fun bindTaskRepository(taskImpl:TaskRepositoryImp):TaskRepository

    @Singleton
    @Provides
    abstract fun bindSessionRepository(sessionImpl:SessionRepositoryImp):SessionRepository

}