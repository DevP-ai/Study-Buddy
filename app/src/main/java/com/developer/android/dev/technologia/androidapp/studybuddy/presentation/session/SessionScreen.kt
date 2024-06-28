package com.developer.android.dev.technologia.androidapp.studybuddy.presentation.session

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components.DeleteDialog
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components.SubjectListBottomSheet
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components.TimerSection
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components.studySessionList
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.theme.Red
import com.developer.android.dev.technologia.androidapp.studybuddy.utils.Constants.ACTION_SERVICE_CANCEL
import com.developer.android.dev.technologia.androidapp.studybuddy.utils.Constants.ACTION_SERVICE_START
import com.developer.android.dev.technologia.androidapp.studybuddy.utils.Constants.ACTION_SERVICE_STOP
import com.developer.android.dev.technologia.androidapp.studybuddy.utils.SnackbarEvent
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.DurationUnit

@Destination(
    deepLinks = [
        DeepLink(
            action = Intent.ACTION_VIEW,
            uriPattern = "study_buddy://dashboard/session"
        )
    ]
)
@Composable
fun SessionScreenRoute(
    navigator: DestinationsNavigator,
    timerService: StudyTimerService
) {
    val viewModel: SessionViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    SessionScreen(
        state = state,
        snackBarEvent = viewModel.snackBarEventFlow,
        onEvent = viewModel::onEvent,
        onBackButtonClick = { navigator.navigateUp() },
        timerService = timerService
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionScreen(
    state: SessionState,
    snackBarEvent:SharedFlow<SnackbarEvent>,
    onEvent: (SessionEvent)-> Unit,
    onBackButtonClick: () -> Unit,
    timerService: StudyTimerService
) {

    val hours by timerService.hours
    val minutes by timerService.minutes
    val seconds by timerService.seconds
    val currentTimerState by timerService.currentTimerState

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isSubjectBottomSheetOpen by rememberSaveable {
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

                SnackbarEvent.NavigateUp -> {}
            }
        }
    }

    LaunchedEffect(key1 = state.subjects) {
        val subjectId = timerService.subjectId.value
        onEvent(
            SessionEvent.UpdateSubjectRelatedSubject(
                subjectId= subjectId,
                relatedToSubject = state.subjects.find { it.subjectId == subjectId }?.name
            )
        )
    }

    val bottomSheetState = rememberModalBottomSheetState()
    SubjectListBottomSheet(
        sheetState = bottomSheetState,
        isOpen = isSubjectBottomSheetOpen,
        subjects = state.subjects,
        onSubjectClick = {subject->
            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                if (!bottomSheetState.isVisible) isSubjectBottomSheetOpen = false
            }
            onEvent(SessionEvent.OnRelatedSubjectChange(subject))
        },
        onDismissRequest = { isSubjectBottomSheetOpen = false }
    )

    var isDeleteDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }
    DeleteDialog(
        title = "Delete Session?",
        isOpen = isDeleteDialogOpen,
        bodyText = "Are you sure, you want to delete this session?\nThis action can not be undone.",
        onDismissRequest = {
            isDeleteDialogOpen = false
        },
        onConfirmClick = {
            onEvent(SessionEvent.DeleteSession)
            isDeleteDialogOpen = false
        })

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            SessionScreenTopBar(
                onBackButtonClick = onBackButtonClick
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
                TimerSection(
                    modifier = Modifier
                        .fillMaxWidth(),
                    hours = hours,
                    minutes = minutes,
                    seconds = seconds,
                    size = 150.dp,
                    fontSize = 16.sp,
                    textStyle = MaterialTheme.typography.titleLarge,
                    stroke = 3.dp,
                    progressColor = Color.Green,
                    isUseCenter=true
                )
            }

            item {
                RelatedToSubjectSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    relatedToSubject =state.relatedToSubject?:"Select a subject",
                    selectedSubjectButtonClick = {
                        isSubjectBottomSheetOpen = true
                    },
                    seconds = seconds
                )
            }

            item {
                ButtonsSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    startButtonClick = {
                        if(state.subjectId !=null && state.relatedToSubject !=null){
                            ServiceHelper.triggerForegroundService(
                                context = context,
                                action = if(currentTimerState == TimerState.STARTED){
                                    ACTION_SERVICE_STOP
                                }else{
                                    ACTION_SERVICE_START
                                }
                            )
                            timerService.subjectId.value = state.subjectId
                        }else{
                            onEvent(SessionEvent.NotifyToUpdateSubject)
                        }
                    },
                    cancelButtonClick = {
                        ServiceHelper.triggerForegroundService(
                            context = context,
                            action = ACTION_SERVICE_CANCEL
                        )
                    },
                    finishedButtonClick = {
                        val duration = timerService.duration.toLong(DurationUnit.SECONDS)
                        if(duration>=10){
                            ServiceHelper.triggerForegroundService(
                                context = context,
                                action = ACTION_SERVICE_CANCEL
                            )
                        }
                        onEvent(SessionEvent.SaveSession(duration))
                    },
                    timerState = currentTimerState,
                    seconds=seconds
                )
            }

            studySessionList(
                sectionTitle = "STUDY SESSIONS HISTORY",
                sessions = state.sessions,
                emptyListText = "You don't have any recent study sessions.\nStart a study session to begin recording your progress.",
                onDeleteClick = {session->
                    isDeleteDialogOpen = true
                    onEvent(SessionEvent.OnDeleteSessionButtonClick(session))
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreenTopBar(
    onBackButtonClick: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackButtonClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Navigation"
                )
            }
        },
        title = {
            Text(
                text = "Study Session",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    )
}

//@Composable
//private fun TimerSection(
//    modifier: Modifier,
//    hours:String,
//    minutes:String,
//    seconds:String
//) {
//    Box(
//        modifier = modifier,
//        contentAlignment = Alignment.Center
//    ) {
//        Box(
//            modifier = Modifier
//                .size(150.dp)
//                .border(5.dp, MaterialTheme.colorScheme.surfaceVariant, CircleShape)
//        )
//        Text(
//            text = "$hours:$minutes:$seconds",
//            style = MaterialTheme.typography.titleLarge.copy(fontSize = 30.sp)
//        )
//    }
//}

//@Composable
//private fun TimerSection(
//    modifier: Modifier = Modifier,
//    hours: String,
//    minutes: String,
//    seconds: String,
//    size:Dp,
//    fontSize:TextUnit,
//    textStyle:TextStyle,
//    stroke:Dp,
//    progressColor: Color,
//    isUseCenter:Boolean
//) {
//    val progress = seconds.toFloat()/60f
//
//    val animatedProgress by animateFloatAsState(
//        targetValue = progress,
//        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
//        label = ""
//    )
//
//    val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant
//
//    Box(
//        modifier = modifier,
//        contentAlignment = Alignment.Center
//    ) {
//        Canvas(modifier = Modifier.size(size)) {
//            drawCircle(
//                color = surfaceVariantColor,
//                style = Stroke(width = stroke.toPx())
//            )
//            drawArc(
//                color = progressColor,
//                startAngle = -90f,
//                sweepAngle = 360 * animatedProgress,
//                useCenter = isUseCenter,
//                style = Stroke(width = stroke.toPx())
//            )
//        }
//        Text(
//            text = "$hours:$minutes:$seconds",
//            style = textStyle.copy(
//                fontSize = fontSize,
//                fontWeight = FontWeight.Bold
//            )
//        )
//    }
//}
//@Composable
//private fun TimerSection(
//    modifier: Modifier = Modifier,
//    hours: String,
//    minutes: String,
//    seconds: String
//) {
//    var cumulativeProgress by remember { mutableStateOf(0f) }
//    val progressIncrement = 1f / 60f
//
//    LaunchedEffect(key1 = seconds) {
//        cumulativeProgress += progressIncrement
//    }
//
//    val animatedProgress by animateFloatAsState(
//        targetValue = cumulativeProgress,
//        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
//        label = "Study Session"
//    )
//
//    val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant
//
//    Box(
//        modifier = Modifier,
//        contentAlignment = Alignment.Center
//    ) {
//        Canvas(modifier = Modifier.size(250.dp)) {
//            drawCircle(
//                color = surfaceVariantColor,
//                style = Stroke(width = 5.dp.toPx())
//            )
//            drawArc(
//                color = Color.Blue,
//                startAngle = -90f,
//                sweepAngle = 360 * animatedProgress,
//                useCenter = true,
//                style = Stroke(width = 5.dp.toPx())
//            )
//        }
//        Text(
//            text = "$hours:$minutes:$seconds",
//            style = MaterialTheme.typography.titleLarge.copy(
//                fontSize = 45.sp,
//                fontWeight = FontWeight.Bold
//            )
//        )
//    }
//}

@Composable
private fun RelatedToSubjectSection(
    modifier: Modifier,
    relatedToSubject: String,
    selectedSubjectButtonClick: () -> Unit,
    seconds: String
) {
    Column(modifier = modifier) {
        Text(
            text = "Related to subject",
            style = MaterialTheme.typography.bodySmall
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = relatedToSubject,
                style = MaterialTheme.typography.bodyLarge
            )
            IconButton(
                onClick = selectedSubjectButtonClick,
                enabled = seconds == "00") {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Select subject"
                )
            }
        }
    }
}

@Composable
fun ButtonsSection(
    modifier: Modifier,
    startButtonClick: () -> Unit,
    cancelButtonClick: () -> Unit,
    finishedButtonClick: () -> Unit,
    timerState: TimerState,
    seconds: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            modifier = Modifier.weight(1f),
            onClick = cancelButtonClick,
            enabled = seconds !="00" && timerState!=TimerState.STARTED,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            )
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 5.dp),
                text = "Cancel"
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Button(
            modifier = Modifier.weight(1f),
            onClick = startButtonClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = if(timerState == TimerState.STARTED) Red
                else Color.Blue,
                contentColor = Color.White
            )

        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 5.dp),
                text = when(timerState){
                    TimerState.STARTED -> "Stop"
                    TimerState.STOPPED ->"Resume"
                    else -> "Start"
                }
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Button(
            modifier = Modifier.weight(1f),
            onClick = finishedButtonClick,
            enabled = seconds !="00" && timerState!=TimerState.STARTED,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            )
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 5.dp),
                text = "Finish"
            )
        }
    }
}