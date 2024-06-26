package com.developer.android.dev.technologia.androidapp.studybuddy

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Session
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Subject
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Task
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.NavGraphs
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.destinations.SessionScreenRouteDestination
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.session.StudyTimerService
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.theme.StudyBuddyTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var isBound by mutableStateOf(false)
    private lateinit var timerService: StudyTimerService

    private val connection = object:ServiceConnection{
        override fun onServiceConnected(p0: ComponentName?,service: IBinder?) {
            val binder = service as StudyTimerService.StudySessionTimerBinder
            timerService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false
        }

    }

    override fun onStart() {
        super.onStart()
        Intent(this,StudyTimerService::class.java).also { intent->
            bindService(intent,connection,Context.BIND_AUTO_CREATE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if(isBound){
                StudyBuddyTheme {
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        dependenciesContainerBuilder = {
                            dependency(SessionScreenRouteDestination){
                                timerService
                            }
                        }
                    )
                }
            }
        }
        requestPermission()
    }

    private fun requestPermission(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        isBound = false
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
