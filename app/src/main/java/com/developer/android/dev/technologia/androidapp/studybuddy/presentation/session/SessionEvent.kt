package com.developer.android.dev.technologia.androidapp.studybuddy.presentation.session

import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Session
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Subject

sealed class SessionEvent {
    data class OnRelatedSubjectChange(val subject: Subject):SessionEvent()

    data class SaveSession(val duration:Long) : SessionEvent()

    data class OnDeleteSessionButtonClick(val session: Session):SessionEvent()

    data object DeleteSession: SessionEvent()

    data object NotifyToUpdateSubject : SessionEvent()

    data class UpdateSubjectRelatedSubject(val subjectId:Int?,val relatedToSubject:String?):SessionEvent()
}