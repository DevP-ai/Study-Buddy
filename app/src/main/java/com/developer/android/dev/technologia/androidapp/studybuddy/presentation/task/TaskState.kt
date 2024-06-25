package com.developer.android.dev.technologia.androidapp.studybuddy.presentation.task

import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Subject
import com.developer.android.dev.technologia.androidapp.studybuddy.utils.Priority

data class TaskState(
    val title:String="",
    val description:String="",
    val dueDate:Long?=null,
    val isTaskComplete:Boolean=false,
    val priority: Priority=Priority.LOW,
    val relatedToSubject:String?=null,
    val subjects:List<Subject> = emptyList(),
    val subjectId:Int?=null,
    val currentTaskId:Int?=null
)
