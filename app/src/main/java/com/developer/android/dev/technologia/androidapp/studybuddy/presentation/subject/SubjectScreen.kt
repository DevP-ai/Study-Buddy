package com.developer.android.dev.technologia.androidapp.studybuddy.presentation.subject

import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
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
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Session
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Subject
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Task
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components.AddSubjectDialog
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components.CountCard
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components.DeleteDialog
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components.studySessionList
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components.taskList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectScreen(

) {
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
            taskId = 1,
            taskSubjectId = 0,
            title = "Prepare Note",
            description = "",
            dueDate = 0L,
            priority = 1,
            relatedToSubject = "",
            isComplete = true
        ),
        Task(
            taskId = 1,
            taskSubjectId = 0,
            title = "Prepare Note",
            description = "",
            dueDate = 0L,
            priority = 2,
            relatedToSubject = "",
            isComplete = true
        ),
        Task(
            taskId = 1,
            taskSubjectId = 0,
            title = "Prepare Note",
            description = "",
            dueDate = 0L,
            priority = 0,
            relatedToSubject = "",
            isComplete = true
        ),
        Task(
            taskId = 1,
            taskSubjectId = 0,
            title = "Prepare Note",
            description = "",
            dueDate = 0L,
            priority = 1,
            relatedToSubject = "",
            isComplete = true
        ),
        Task(
            taskId = 1,
            taskSubjectId = 0,
            title = "Prepare Note",
            description = "",
            dueDate = 0L,
            priority = 2,
            relatedToSubject = "",
            isComplete = true
        ),
        Task(
            taskId = 1,
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
        Session(0,"Physics",0L,0L,1),
        Session(0,"Physics",0L,0L,1),
        Session(0,"Physics",0L,0L,1),
        Session(0,"Physics",0L,0L,1),
        Session(0,"Physics",0L,0L,1),
        Session(0,"Physics",0L,0L,1),
        Session(0,"Physics",0L,0L,1),
        Session(0,"Physics",0L,0L,1)
    )


    var isEditSubjectDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }

    var subjectName by remember { mutableStateOf("")  }

    var goalHours by remember { mutableStateOf("") }

    var selectedColor by remember { mutableStateOf(Subject.subjectColors.random()) }

    AddSubjectDialog(
        isOpen = isEditSubjectDialogOpen,
        onDismissRequest = { isEditSubjectDialogOpen=false },
        onConfirmClick = {
            isEditSubjectDialogOpen=false
        },
        selectedColor=selectedColor,
        onColorChange = {
            selectedColor=it
        },
        subjectName =subjectName ,
        goalHours = goalHours,
        onSubjectNameChange = {
            subjectName=it
        },
        onGoalHourChange = {
            goalHours = it
        }
    )


    var isDeleteDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }
    DeleteDialog(
        title = "Delete Session?",
        isOpen = isDeleteDialogOpen,
        bodyText ="Are you sure, you want to delete this session?\nYour studied hours will be reduced by this\nsession time. This action can not be undone." ,
        onDismissRequest = {
            isDeleteDialogOpen=false},
        onConfirmClick = {
            isDeleteDialogOpen=false
        })

    var isSubjectDeleteDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }
    DeleteDialog(
        title = "Delete Subject?",
        isOpen = isSubjectDeleteDialogOpen,
        bodyText ="Are you sure, you want to delete this subject?All related tasks and study sessions will be permanently removed.This action can not be undone." ,
        onDismissRequest = {
            isSubjectDeleteDialogOpen=false},
        onConfirmClick = {
            isSubjectDeleteDialogOpen=false
        })

    val listState = rememberLazyListState()

    val isFabExpanded by remember {
        derivedStateOf { listState.firstVisibleItemIndex ==0 }
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SubjectScreenTopBar(
                title ="English",
                onBackButtonClick = { /*TODO*/ },
                onDeleteButtonClick = { isSubjectDeleteDialogOpen=true },
                onEditButtonClick = {isEditSubjectDialogOpen=true},
                scrollBehavior = scrollBehavior
                )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { /*TODO*/ },
                icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "Add Tasks")},
                text = { Text(text = "Add Tasks")},
                expanded = isFabExpanded
            )
        }
    ) {paddingValues ->
        LazyColumn (
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            item{
                SubjectOverviewSection(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    studiedHours = "10",
                    goalHours ="20" ,
                    progress = 0.75f
                )
            }

            taskList(
                sectionTitle = "UPCOMING TASKS",
                tasks = tasks,
                emptyListText = "You don't have any upcoming tasks.\nClick the + button in subject screen to add new task.",
                onTaskCardClick = {},
                onCheckBoxClick = {}
            )
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            taskList(
                sectionTitle = "COMPLETED TASKS",
                tasks = emptyList(),
                emptyListText = "You don't have any completed tasks.\nClick the check box on completion of tasks.",
                onTaskCardClick = {},
                onCheckBoxClick = {}
            )
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            studySessionList(
                sectionTitle = "RECENT STUDY SESSION",
                sessions = sessions,
                emptyListText = "You don't have any recent study sessions.\nStart a study session to begin recording your progress.",
                onDeleteClick = {
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
    onBackButtonClick:()->Unit,
    onDeleteButtonClick:()->Unit,
    onEditButtonClick:()->Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    LargeTopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = { onBackButtonClick() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription ="Navigation Back" )
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
                    contentDescription ="Delete Subject"
                )
            }
            IconButton(onClick = { onEditButtonClick() }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription ="Edit Subject"
                )
            }
        }
    )
}

@Composable
private fun SubjectOverviewSection(
    modifier: Modifier,
    studiedHours:String,
    goalHours:String,
    progress:Float
) {
    val progressPercentage = remember(progress) {
        (progress*100).toInt().coerceIn(0,100)
    }
    Row (
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ){
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
        ){
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