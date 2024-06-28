package com.developer.android.dev.technologia.androidapp.studybuddy.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Session(
    val sessionSubjectId:Int,
    val relatedToSubject:String,
    val date:Long,
//    val subjectName:String,
    val duration:Long,
    @PrimaryKey(autoGenerate = true)
    val sessionId:Int?=null
)
