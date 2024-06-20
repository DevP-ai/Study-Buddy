package com.developer.android.dev.technologia.androidapp.studybuddy.data.repository

import com.developer.android.dev.technologia.androidapp.studybuddy.data.local.TaskDao
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Task
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImp @Inject constructor(
    private val taskDao: TaskDao
):TaskRepository{
    override suspend fun upsertTask(task: Task) {
        taskDao.upsertTask(task)
    }

    override suspend fun deleteTask(taskId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskById(taskId: Int): Task? {
        TODO("Not yet implemented")
    }

    override fun getUpcomingTasksForSubject(subjectId: Int): Flow<List<Task>> {
        return taskDao.getTasksForSubject(subjectId)
            .map { tasks->
                tasks.filter { it.isComplete.not() }
            }
            .map {tasks->
                sortTasks(tasks)
            }
    }

    override fun getCompletedTasksForSubject(subjectId: Int): Flow<List<Task>> {
        return taskDao.getTasksForSubject(subjectId)
            .map { tasks->
                tasks.filter { it.isComplete}
            }
            .map {tasks->
                sortTasks(tasks)
            }
    }

    override fun getAllUpcomingTasks(): Flow<List<Task>> {
        return  taskDao.getAllTasks()
            .map { tasks->
                tasks.filter {
                    it.isComplete.not()
                }
            }
            .map {tasks->
                sortTasks(tasks)
            }
    }

    private fun sortTasks(tasks:List<Task>):List<Task>{
        return tasks.sortedWith(compareBy<Task>{
            it.dueDate
        })
    }

}