package com.developer.android.dev.technologia.androidapp.studybuddy.presentation.task

import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Subject
import com.developer.android.dev.technologia.androidapp.studybuddy.utils.Priority

sealed class TaskEvent {
    data class OnTitleChange(val title:String):TaskEvent()
    data class OnDescriptionChange(val description:String):TaskEvent()
    data class OnDateChange(val mills:Long?):TaskEvent()
    data class OnPriorityChange(val priority: Priority):TaskEvent()
    data class OnRelatedSubjectSelect(val subject:Subject):TaskEvent()
    data object OnIsCompleteChange:TaskEvent()
    data object SaveTask:TaskEvent()
    data object DeleteTask:TaskEvent()
}