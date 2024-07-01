package com.developer.android.dev.technologia.androidapp.studybuddy.presentation.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.developer.android.dev.technologia.androidapp.studybuddy.R
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Session
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Subject
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Task
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components.AddSubjectDialog
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components.ChartSection
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components.CountCard
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components.DeleteDialog
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components.SubjectCard
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components.TimerSection
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components.studySessionList
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components.taskList
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.destinations.SessionScreenRouteDestination
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.destinations.SettingsScreenRouteDestination
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.destinations.SubjectScreenRouteDestination
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.destinations.TaskScreenRouteDestination
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.session.StudyTimerService
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.session.TimerState
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.settings.SettingsScreenRoute
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.subject.SubjectScreenNavArgs
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.task.TaskScreenNavArgs
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.theme.gradient4
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.theme.md_theme_dark_primary
import com.developer.android.dev.technologia.androidapp.studybuddy.utils.PieChartDataPoint
import com.developer.android.dev.technologia.androidapp.studybuddy.utils.SnackbarEvent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@RootNavGraph(start = true)
@Destination
@Composable
fun DashboardScreenRoute(
    navigator: DestinationsNavigator
) {
    val viewModel: DashboardViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val recentSessions by viewModel.recentSessions.collectAsState()

    DashboardScreen(
        state = state,
        tasks = tasks,
        recentSessions = recentSessions,
        onEvent = viewModel::onEvent,
        navigator = navigator,
        snackBarEvent = viewModel.snackBarEventFlow,
        onSubjectCardClick = { subjectId ->
            subjectId?.let {
                val navArg = SubjectScreenNavArgs(subjectId = subjectId)
                navigator.navigate(SubjectScreenRouteDestination(navArgs = navArg))
            }
        },
        onTaskCardClick = { taskId ->
            val navArg = TaskScreenNavArgs(taskId = taskId, subjectId = null)
            navigator.navigate(TaskScreenRouteDestination(navArgs = navArg))
        },
        onStartSessionClick = {
            navigator.navigate(SessionScreenRouteDestination())
        }
    )
}


@Composable
private fun DashboardScreen(
    state: DashboardState,
    tasks: List<Task>,
    recentSessions: List<Session>,
    navigator: DestinationsNavigator,
    snackBarEvent: SharedFlow<SnackbarEvent>,
    onEvent: (DashboardEvent) -> Unit,
    onSubjectCardClick: (Int?) -> Unit,
    onTaskCardClick: (Int?) -> Unit,
    onStartSessionClick: () -> Unit
) {


    var isAddSubjectDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }
    var isDeleteDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }

    val snackBarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(key1 = true) {
        snackBarEvent.collectLatest { event ->
            when (event) {
                is SnackbarEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = event.message,
                        duration = event.duration
                    )
                }

                SnackbarEvent.NavigateUp -> {}
            }
        }
    }
    AddSubjectDialog(
        isOpen = isAddSubjectDialogOpen,
        onDismissRequest = { isAddSubjectDialogOpen = false },
        onConfirmClick = {
            onEvent(DashboardEvent.SaveSubject)
            isAddSubjectDialogOpen = false
        },
        selectedColor = state.subjectCardColor,
        onColorChange = {
            onEvent(DashboardEvent.OnSubjectCarColorChange(it))
        },
        subjectName = state.subjectName,
        goalHours = state.goalStudyHours,
        onSubjectNameChange = {
            onEvent(DashboardEvent.OnSubjectNameChange(it))
        },
        onGoalHourChange = {
            onEvent(DashboardEvent.OnGoalStudyHourChange(it))
        }
    )

    DeleteDialog(
        title = "Delete Session?",
        isOpen = isDeleteDialogOpen,
        bodyText = "Are you sure, you want to delete this session?\nYour studied hours will be reduced by this\nsession time. This action can not be undone.",
        onDismissRequest = {
            isDeleteDialogOpen = false
        },
        onConfirmClick = {
            onEvent(DashboardEvent.DeleteSession)
            isDeleteDialogOpen = false
        })

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            DashboardTopAppBar(onClickSettings={
                navigator.navigate(SettingsScreenRouteDestination())
            })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                CountCardSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    subjectCount = state.totalSubjectCount,
                    studiedHours = state.totalStudiedHours.toString(),
                    goalHours = state.totalGoalStudyHours.toString()
                )
            }
            val chartData = listOf(
                PieChartDataPoint(
                    value = state.totalGoalStudyHours,
                    title = "Goal Study Hours",
                    color = Color.Green
                ),
                PieChartDataPoint(
                    value = state.totalStudiedHours,
                    title = "Total Studied Hours",
                    color = Color.Blue
                )
            )
            item {
                ChartSection(
                    data = chartData,
                    studiedHours = state.totalStudiedHours.toString(),
                    goalHours = state.totalGoalStudyHours.toString()
                )
            }

            item {
                SubjectCardSection(
                    modifier = Modifier.fillMaxWidth(),
                    subjectList = state.subjects,
                    onAddIClick = {
                        isAddSubjectDialogOpen = true
                    },
                    onSubjectCardClick = onSubjectCardClick
                )
            }

            item {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp, vertical = 20.dp),
                    onClick = onStartSessionClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Start Study Session")
                }
            }



            taskList(
                sectionTitle = "UPCOMING TASKS",
                tasks = tasks,
                emptyListText = "You don't have any upcoming tasks.\nClick the + button in subject screen to add new task.",
                onTaskCardClick = onTaskCardClick,
                onCheckBoxClick = {
                    onEvent(DashboardEvent.OnTaskIsCompleteChange(it))
                }
            )
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            studySessionList(
                sectionTitle = "RECENT STUDY SESSION",
                sessions = recentSessions,
                emptyListText = "You don't have any recent study sessions.\nStart a study session to begin recording your progress.",
                onDeleteClick = {
                    onEvent(DashboardEvent.OnDeleteSessionButtonClick(it))
                    isDeleteDialogOpen = true
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardTopAppBar(
    onClickSettings:() ->Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Study Buddy",
            style = MaterialTheme.typography.headlineMedium,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier.padding(start = 12.dp)
        )
        IconButton(onClick ={onClickSettings()}) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings"
            )
        }
    }
}

@Composable
private fun CountCardSection(
    modifier: Modifier,
    subjectCount: Int,
    studiedHours: String,
    goalHours: String
) {
    Row(modifier = modifier) {
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Subject Count",
            count = "$subjectCount"
        )

        Spacer(modifier = Modifier.width(10.dp))

        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Studied Hours",
            count = studiedHours
        )

        Spacer(modifier = Modifier.width(10.dp))

        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Goal Study Hour",
            count = goalHours
        )

    }

}

@Composable
private fun SubjectCardSection(
    modifier: Modifier,
    subjectList: List<Subject>,
    emptyListText: String = "You don't have any subjects.\nClick + button to add new subject.",
    onAddIClick: () -> Unit,
    onSubjectCardClick: (Int?) -> Unit
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "SUBJECTS",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 12.dp)
            )
            IconButton(onClick = { onAddIClick() }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Subject"
                )
            }
        }

        if (subjectList.isEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.img_books),
                contentDescription = emptyListText,
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = emptyListText,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
        ) {
            items(subjectList) { subject ->
                SubjectCard(
                    onClick = { onSubjectCardClick(subject.subjectId) },
                    gradientColors = subject.colors.map { Color(it) },
                    subjectName = subject.name
                )
            }
        }
    }
}