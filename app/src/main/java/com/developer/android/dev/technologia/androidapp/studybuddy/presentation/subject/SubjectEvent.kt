package com.developer.android.dev.technologia.androidapp.studybuddy.presentation.subject

import androidx.compose.ui.graphics.Color
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Session
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Task

sealed class SubjectEvent {
    data object UpdatedSubject:SubjectEvent()
    data object DeletedSubject:SubjectEvent()
    data object DeletedSession:SubjectEvent()
    data object UpdateProgress:SubjectEvent()
    data class  OnTaskIsCompleteChange(val task:Task):SubjectEvent()
    data class  OnSubjectCardColorChange(val color:List<Color>):SubjectEvent()
    data class  OnSubjectNameChange(val name:String):SubjectEvent()
    data class OnGoalStudyHoursChange(val hours:String):SubjectEvent()
    data class OnDeleteSessionsButtonClick(val session: Session):SubjectEvent()
}