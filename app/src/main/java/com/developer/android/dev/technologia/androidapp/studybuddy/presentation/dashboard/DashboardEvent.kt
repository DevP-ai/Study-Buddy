package com.developer.android.dev.technologia.androidapp.studybuddy.presentation.dashboard

import androidx.compose.ui.graphics.Color
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Session
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Task

sealed class DashboardEvent {
    data object SaveSubject : DashboardEvent()
    data object DeleteSession : DashboardEvent()
    data class OnDeleteSessionButtonClick(val session: Session) : DashboardEvent()
    data class OnTaskIsCompleteChange(val task: Task) : DashboardEvent()
    data class OnSubjectCarColorChange(val colors: List<Color>) : DashboardEvent()
    data class OnSubjectNameChange(val name: String) : DashboardEvent()
    data class OnGoalStudyHourChange(val hours: String) : DashboardEvent()
}