package com.developer.android.dev.technologia.androidapp.studybuddy.utils

import androidx.compose.ui.graphics.Color
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.theme.Green
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.theme.Orange
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.theme.Red


enum class Priority(val title:String, val color: Color,val value:Int){
    LOW(title = "Low",color= Green, value=0),
    MEDIUM(title = "Medium",color= Orange, value=1),
    HIGH(title = "High",color= Red, value=2);

    companion object{
        fun fromInt(value: Int) = entries.firstOrNull {
            it.value == value
        }?:MEDIUM
    }

}