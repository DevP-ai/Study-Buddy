package com.developer.android.dev.technologia.androidapp.studybuddy.domain.model

import androidx.compose.ui.graphics.Color
import androidx.room.PrimaryKey
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.theme.card
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.theme.gradient1
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.theme.gradient2
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.theme.gradient3
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.theme.gradient4
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.theme.gradient5

data class Subject(
    val name:String,
    val goalHours:Float,
    val colors:List<Color>
){
    companion object{
        val subjectColors = listOf(gradient1, gradient2, gradient3, gradient4, gradient5, card)
    }
}