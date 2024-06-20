package com.developer.android.dev.technologia.androidapp.studybuddy.presentation.subject

import androidx.compose.ui.graphics.Color
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Session
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Subject
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Task

data class SubjectState(
    val currentSubjectId:Int?=null,
    val subjectName:String="",
    val goalStudyHours:String="",
    val subjectCardColors:List<Color> = Subject.subjectColors.random(),
    val studiedHours:Float=0f,
    val progress:Float=0f,
    val recentSession:List<Session> = emptyList(),
    val upcomingTasks:List<Task> = emptyList(),
    val completedTasks:List<Task> = emptyList(),
    val session: Session?=null,
//    val isLoading:Boolean=false
)
