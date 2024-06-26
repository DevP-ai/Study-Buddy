package com.developer.android.dev.technologia.androidapp.studybuddy.presentation.session

import android.content.Intent
import android.graphics.Color.RED
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components.DeleteDialog
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components.SubjectListBottomSheet
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components.studySessionList
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.theme.Red
import com.developer.android.dev.technologia.androidapp.studybuddy.sessions
import com.developer.android.dev.technologia.androidapp.studybuddy.subjects
import com.developer.android.dev.technologia.androidapp.studybuddy.utils.Constants.ACTION_SERVICE_CANCEL
import com.developer.android.dev.technologia.androidapp.studybuddy.utils.Constants.ACTION_SERVICE_START
import com.developer.android.dev.technologia.androidapp.studybuddy.utils.Constants.ACTION_SERVICE_STOP
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Timer

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
    SessionScreen(
        onBackButtonClick = { navigator.navigateUp() },
        timerService = timerService
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionScreen(
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

    val bottomSheetState = rememberModalBottomSheetState()
    SubjectListBottomSheet(
        sheetState = bottomSheetState,
        isOpen = isSubjectBottomSheetOpen,
        subjects = subjects,
        onSubjectClick = {
            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                if (!bottomSheetState.isVisible) isSubjectBottomSheetOpen = false
            }
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
            isDeleteDialogOpen = false
        })

    Scaffold(
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
                    stroke = 3.dp
                )
            }

            item {
                RelatedToSubjectSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    relatedToSubject = "English",
                    selectedSubjectButtonClick = {
                        isSubjectBottomSheetOpen = true
                    }
                )
            }

            item {
                ButtonsSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    startButtonClick = {
                        ServiceHelper.triggerForegroundService(
                            context = context,
                            action = if(currentTimerState == TimerState.STARTED){
                                ACTION_SERVICE_STOP
                            }else{
                                ACTION_SERVICE_START
                            }
                        )
                    },
                    cancelButtonClick = {
                        ServiceHelper.triggerForegroundService(
                            context = context,
                            action = ACTION_SERVICE_CANCEL
                        )
                    },
                    finishedButtonClick = {
                        ServiceHelper.triggerForegroundService(
                            context = context,
                            action = ACTION_SERVICE_STOP
                        )
                    },
                    timerState = currentTimerState,
                    seconds=seconds
                )
            }

            studySessionList(
                sectionTitle = "STUDY SESSIONS HISTORY",
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

@Composable
private fun TimerSection(
    modifier: Modifier = Modifier,
    hours: String,
    minutes: String,
    seconds: String,
    size:Dp,
    fontSize:TextUnit,
    textStyle:TextStyle,
    stroke:Dp
) {
    val progress = seconds.toFloat()/60f

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = ""
    )

    val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            drawCircle(
                color = surfaceVariantColor,
                style = Stroke(width = stroke.toPx())
            )
            drawArc(
                color = Color.Blue,
                startAngle = -90f,
                sweepAngle = 360 * animatedProgress,
                useCenter = true,
                style = Stroke(width = stroke.toPx())
            )
        }
        Text(
            text = "$hours:$minutes:$seconds",
            style = textStyle.copy(
                fontSize = fontSize,
                fontWeight = FontWeight.Bold
            )
        )
    }
}
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
    selectedSubjectButtonClick: () -> Unit
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
            IconButton(onClick = selectedSubjectButtonClick) {
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
            onClick = cancelButtonClick,
            enabled = seconds !="00" && timerState!=TimerState.STARTED
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                text = "Cancel"
            )
        }
        Button(
            onClick = startButtonClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = if(timerState == TimerState.STARTED) Red
                else MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )

        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                text = when(timerState){
                    TimerState.STARTED -> "Stop"
                    TimerState.STOPPED ->"Resume"
                    else -> "Start"
                }
            )
        }
        Button(
            onClick = finishedButtonClick,
            enabled = seconds !="00" && timerState!=TimerState.STARTED
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                text = "Finish"
            )
        }
    }
}