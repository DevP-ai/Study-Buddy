package com.developer.android.dev.technologia.androidapp.studybuddy.data.repository

import com.developer.android.dev.technologia.androidapp.studybuddy.data.local.SessionDao
import com.developer.android.dev.technologia.androidapp.studybuddy.data.local.SubjectDao
import com.developer.android.dev.technologia.androidapp.studybuddy.data.local.TaskDao
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Subject
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubjectRepositoryImp @Inject constructor(
    private val subjectDao: SubjectDao,
    private val sessionDao: SessionDao,
    private val taskDao: TaskDao
):SubjectRepository{
    override suspend fun upsertSubject(subject: Subject) {
        subjectDao.upsertSubject(subject)
    }

    override fun getTotalSubjectCount(): Flow<Int> {
        return subjectDao.getTotalSubjectCount()
    }

    override fun getTotalGoalHours(): Flow<Float> {
        return subjectDao.getTotalGoalHours()
    }

    override suspend fun getSubjectById(subjectId: Int): Subject? {
        return subjectDao.getSubjectById(subjectId=subjectId)
    }

    override suspend fun deleteSubject(subjectId: Int) {
        taskDao.deleteTaskBySubjectId(subjectId = subjectId)
        sessionDao.deleteSessionBySubjectId(subjectId = subjectId)
        subjectDao.deleteSubject(subjectId=subjectId)
    }

    override fun getAllSubjects(): Flow<List<Subject>> {
        return subjectDao.getAllSubjects()
    }
}