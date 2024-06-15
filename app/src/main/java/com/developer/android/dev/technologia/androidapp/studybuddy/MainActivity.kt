package com.developer.android.dev.technologia.androidapp.studybuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.toArgb
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Session
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Subject
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Task
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.NavGraphs
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.theme.StudyBuddyTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

val subjects = listOf(
    Subject(
        subjectId = 0,
        name = "English",
        goalHours = 10f,
        colors = Subject.subjectColors[0].map { it.toArgb() }
    ),
    Subject(
        subjectId = 1,
        name = "English",
        goalHours = 10f,
        colors = Subject.subjectColors[1].map { it.toArgb() }
    ),
    Subject(
        subjectId = 2,
        name = "English",
        goalHours = 10f,
        colors = Subject.subjectColors[2].map { it.toArgb() }
    ),
    Subject(
        subjectId = 3,
        name = "English",
        goalHours = 10f,
        colors = Subject.subjectColors[3].map { it.toArgb() }
    ),
    Subject(
        subjectId = 4,
        name = "English",
        goalHours = 10f,
        colors = Subject.subjectColors[4].map { it.toArgb() })
)

val tasks = listOf(
    Task(
        taskId = 1,
        taskSubjectId = 0,
        title = "Prepare Note",
        description = "",
        dueDate = 0L,
        priority = 0,
        relatedToSubject = "",
        isComplete = false
    ),
    Task(
        taskId = 2,
        taskSubjectId = 0,
        title = "Prepare Note",
        description = "",
        dueDate = 0L,
        priority = 1,
        relatedToSubject = "",
        isComplete = true
    ),
    Task(
        taskId = 3,
        taskSubjectId = 0,
        title = "Prepare Note",
        description = "",
        dueDate = 0L,
        priority = 2,
        relatedToSubject = "",
        isComplete = true
    ),
    Task(
        taskId = 4,
        taskSubjectId = 0,
        title = "Prepare Note",
        description = "",
        dueDate = 0L,
        priority = 0,
        relatedToSubject = "",
        isComplete = true
    ),
    Task(
        taskId = 5,
        taskSubjectId = 0,
        title = "Prepare Note",
        description = "",
        dueDate = 0L,
        priority = 1,
        relatedToSubject = "",
        isComplete = true
    ),
    Task(
        taskId = 6,
        taskSubjectId = 0,
        title = "Prepare Note",
        description = "",
        dueDate = 0L,
        priority = 2,
        relatedToSubject = "",
        isComplete = true
    ),
    Task(
        taskId = 7,
        taskSubjectId = 0,
        title = "Prepare Note",
        description = "",
        dueDate = 0L,
        priority = 1,
        relatedToSubject = "",
        isComplete = true
    )
)

val sessions = listOf(
    Session(0, "Physics", 0L, 0L, 1),
    Session(1, "Physics", 0L, 0L, 1),
    Session(2, "Physics", 0L, 0L, 1),
    Session(3, "Physics", 0L, 0L, 1),
    Session(4, "Physics", 0L, 0L, 1),
    Session(5, "Physics", 0L, 0L, 1),
    Session(6, "Physics", 0L, 0L, 1),
    Session(7, "Physics", 0L, 0L, 1)
)
