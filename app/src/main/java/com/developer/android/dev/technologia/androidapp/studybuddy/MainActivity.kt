package com.developer.android.dev.technologia.androidapp.studybuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.dashboard.DashboardScreen
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.subject.SubjectScreen
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.theme.StudyBuddyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudyBuddyTheme {
//                DashboardScreen()
                SubjectScreen()
            }
        }
    }
}
