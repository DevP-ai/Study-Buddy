package com.developer.android.dev.technologia.androidapp.studybuddy.presentation.dashboard

import androidx.compose.ui.graphics.Color
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Session
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Subject

data class DashboardState(
    val totalSubjectCount:Int=0,
    val totalStudiedHours:Float=0f,
    val totalGoalStudyHours:Float=0f,
    val subjects:List<Subject> = emptyList(),
    val subjectName:String="",
    val goalStudyHours:String="",
    val subjectCardColor:List<Color> = Subject.subjectColors.random(),
    val session:Session?=null
)
