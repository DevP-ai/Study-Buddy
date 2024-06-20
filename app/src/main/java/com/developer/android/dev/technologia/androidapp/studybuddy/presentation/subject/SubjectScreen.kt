package com.developer.android.dev.technologia.androidapp.studybuddy.presentation.subject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components.AddSubjectDialog
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components.CountCard
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components.DeleteDialog
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components.studySessionList
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components.taskList
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.destinations.TaskScreenRouteDestination
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.task.TaskScreenNavArgs
import com.developer.android.dev.technologia.androidapp.studybuddy.utils.SnackbarEvent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

data class SubjectScreenNavArgs(
    val subjectId: Int
)

@Destination(navArgsDelegate = SubjectScreenNavArgs::class)
@Composable
fun SubjectScreenRoute(
    navigator: DestinationsNavigator
) {
    val viewModel:SubjectViewModel= hiltViewModel()
    val state by viewModel.state.collectAsState()

    SubjectScreen(
        state = state,
        onEvent = viewModel::onEvent,
        snackBarEvent = viewModel.snackBarEventFlow,
        onBackButtonClick = {navigator.navigateUp()},
        onAddTaskButtonClick = {
            val navArgs=TaskScreenNavArgs(taskId=null,subjectId = state.currentSubjectId)
            navigator.navigate(TaskScreenRouteDestination(navArgs=navArgs))
        },
        onTaskCardClick = {taskId->
            val navArgs=TaskScreenNavArgs(taskId=taskId,subjectId = null)
            navigator.navigate(TaskScreenRouteDestination(navArgs=navArgs))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubjectScreen(
    state: SubjectState,
    onEvent: (SubjectEvent)->Unit,
    snackBarEvent: SharedFlow<SnackbarEvent>,
    onBackButtonClick: () -> Unit,
    onAddTaskButtonClick: () -> Unit,
    onTaskCardClick: (Int?) -> Unit
) {

    var isEditSubjectDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }

    val snackBarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(key1 = true) {
        snackBarEvent.collectLatest { event ->
            when (event) {
                is SnackbarEvent.ShowSnackBar ->{
                    snackBarHostState.showSnackbar(
                        message = event.message,
                        duration = event.duration
                    )
                }

                SnackbarEvent.NavigateUp -> {
                    onBackButtonClick()
                }
            }
        }
    }

    LaunchedEffect(key1 = state.studiedHours, key2 = state.goalStudyHours) {
        onEvent(SubjectEvent.UpdateProgress)
    }


//    var subjectName by remember { mutableStateOf("") }
//    var goalHours by remember { mutableStateOf("") }
//    var selectedColor by remember { mutableStateOf(Subject.subjectColors.random()) }

    AddSubjectDialog(
        isOpen = isEditSubjectDialogOpen,
        onDismissRequest = { isEditSubjectDialogOpen = false },
        onConfirmClick = {
            onEvent(SubjectEvent.UpdatedSubject)
            isEditSubjectDialogOpen = false
        },
        selectedColor = state.subjectCardColors,
        onColorChange = {
            onEvent(SubjectEvent.OnSubjectCardColorChange(it))
        },
        subjectName = state.subjectName,
        goalHours = state.goalStudyHours,
        onSubjectNameChange = {
            onEvent(SubjectEvent.OnSubjectNameChange(it))
        },
        onGoalHourChange = {
            onEvent(SubjectEvent.OnGoalStudyHoursChange(it))
        }
    )


    var isDeleteDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }
    DeleteDialog(
        title = "Delete Session?",
        isOpen = isDeleteDialogOpen,
        bodyText = "Are you sure, you want to delete this session?\nYour studied hours will be reduced by this\nsession time. This action can not be undone.",
        onDismissRequest = {
            isDeleteDialogOpen = false
        },
        onConfirmClick = {
            onEvent(SubjectEvent.DeletedSession)
            isDeleteDialogOpen = false
        })

    var isSubjectDeleteDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }
    DeleteDialog(
        title = "Delete Subject?",
        isOpen = isSubjectDeleteDialogOpen,
        bodyText = "Are you sure, you want to delete this subject?All related tasks and study sessions will be permanently removed.This action can not be undone.",
        onDismissRequest = {
            isSubjectDeleteDialogOpen = false
        },
        onConfirmClick = {
            onEvent(SubjectEvent.DeletedSubject)
            isSubjectDeleteDialogOpen = false
            onBackButtonClick()

        }
    )

    val listState = rememberLazyListState()

    val isFabExpanded by remember {
        derivedStateOf { listState.firstVisibleItemIndex == 0 }
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState)},
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SubjectScreenTopBar(
                title = state.subjectName,
                onBackButtonClick = onBackButtonClick,
                onDeleteButtonClick = { isSubjectDeleteDialogOpen = true },
                onEditButtonClick = { isEditSubjectDialogOpen = true },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddTaskButtonClick,
                icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "Add Tasks") },
                text = { Text(text = "Add Tasks") },
                expanded = isFabExpanded
            )
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                SubjectOverviewSection(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    studiedHours = state.studiedHours.toString(),
                    goalHours = state.goalStudyHours,
                    progress = state.progress
                )
            }

            taskList(
                sectionTitle = "UPCOMING TASKS",
                tasks = state.upcomingTasks,
                emptyListText = "You don't have any upcoming tasks.\nClick the + button in subject screen to add new task.",
                onTaskCardClick = {taskId ->
                    onTaskCardClick(taskId)},
                onCheckBoxClick = {
                    onEvent(SubjectEvent.OnTaskIsCompleteChange(it))
                }
            )
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            taskList(
                sectionTitle = "COMPLETED TASKS",
                tasks = state.completedTasks,
                emptyListText = "You don't have any completed tasks.\nClick the check box on completion of tasks.",
                onTaskCardClick = { taskId ->
                    onTaskCardClick(taskId)
                },
                onCheckBoxClick = {
                    onEvent(SubjectEvent.OnTaskIsCompleteChange(it))
                }
            )
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            studySessionList(
                sectionTitle = "RECENT STUDY SESSION",
                sessions = state.recentSession,
                emptyListText = "You don't have any recent study sessions.\nStart a study session to begin recording your progress.",
                onDeleteClick = {
                    onEvent(SubjectEvent.OnDeleteSessionsButtonClick(it))
                    isDeleteDialogOpen = true
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubjectScreenTopBar(
    title: String,
    onBackButtonClick: () -> Unit,
    onDeleteButtonClick: () -> Unit,
    onEditButtonClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    LargeTopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = { onBackButtonClick() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Navigation Back"
                )
            }
        },
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        actions = {
            IconButton(onClick = { onDeleteButtonClick() }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Subject"
                )
            }
            IconButton(onClick = { onEditButtonClick() }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Subject"
                )
            }
        }
    )
}

@Composable
private fun SubjectOverviewSection(
    modifier: Modifier,
    studiedHours: String,
    goalHours: String,
    progress: Float
) {
    val progressPercentage = remember(progress) {
        (progress * 100).toInt().coerceIn(0, 100)
    }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Goal Study Hours",
            count = goalHours
        )
        Spacer(modifier = Modifier.width(10.dp))

        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Study Hours",
            count = studiedHours
        )
        Spacer(modifier = Modifier.width(10.dp))

        Box(
            modifier = Modifier.size(75.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                progress = 1f,
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                progress = progress,
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round
            )
            Text(text = "$progressPercentage")
        }
    }
}