package com.developer.android.dev.technologia.androidapp.studybuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.NavGraph
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.NavGraphs
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.theme.StudyBuddyTheme
import com.ramcosta.composedestinations.DestinationsNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudyBuddyTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}
