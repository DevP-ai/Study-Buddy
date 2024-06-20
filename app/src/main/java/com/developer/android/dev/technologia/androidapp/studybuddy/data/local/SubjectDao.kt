package com.developer.android.dev.technologia.androidapp.studybuddy.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Subject
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {

    @Upsert
    suspend fun upsertSubject(subject: Subject)

    @Query("SELECT COUNT(*) FROM SUBJECT")
    fun getTotalSubjectCount():Flow<Int>

    @Query("SELECT SUM(goalHours) FROM SUBJECT")
    fun getTotalGoalHours():Flow<Float>

    @Query("SELECT * FROM SUBJECT WHERE subjectId=:subjectId")
    suspend fun getSubjectById(subjectId:Int):Subject?

    @Query("DELETE FROM SUBJECT WHERE subjectId=:subjectId")
    suspend fun deleteSubject(subjectId: Int)

    @Query("SELECT * FROM SUBJECT")
    fun getAllSubjects():Flow<List<Subject>>

    /**
     * Above using some function suspend and some are Flow
     * because some functions are one time action so used suspend
     * and some are need continuous action so used Flow
     * **/

    //    @Upsert
//    fun upsertSubject(subject: Subject)
//
//    @Query("SELECT COUNT(*) FROM SUBJECT")
//    fun getTotalSubjectCount():Int
//
//    @Query("SELECT SUM(goalHours) FROM SUBJECT")
//    fun getTotalGoalHours():Float
//
//    @Query("SELECT * FROM SUBJECT WHERE subjectId=:subjectId")
//    fun getSubjectById(subjectId:Int):Subject?
//
//    @Query("DELETE FROM SUBJECT WHERE subjectId=:subjectId")
//    fun deleteSubject(subjectId: Int)
//
//    @Query("SELECT * FROM SUBJECT")
//    fun getAllSubjects():List<Subject>




}