package com.developer.android.dev.technologia.androidapp.studybuddy.domain.repository

import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Session
import kotlinx.coroutines.flow.Flow

interface SessionRepository {

    suspend fun insertSession(session: Session)

    suspend fun deleteSession(session: Session)

    fun getAllSessions():Flow<List<Session>>

    fun getRecentFiveSessions():Flow<List<Session>>

    fun getRecentTenSessionsForSubject(subjectId:Int):Flow<List<Session>>

    fun getTotalSessionsDuration():Flow<Long>

    fun getTotalSessionsDurationBySubject(subjectId: Int):Flow<Long>
}