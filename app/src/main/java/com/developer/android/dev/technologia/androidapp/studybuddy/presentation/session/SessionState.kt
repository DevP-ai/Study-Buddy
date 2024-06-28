package com.developer.android.dev.technologia.androidapp.studybuddy.presentation.session

import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Session
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Subject

data class SessionState(
    val subjects:List<Subject> = emptyList(),
    val sessions:List<Session> = emptyList(),
    val relatedToSubject:String?=null,
    val subjectName:String?=null,
    val subjectId:Int?=null,
    val session: Session?=null
)
