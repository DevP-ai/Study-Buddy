package com.developer.android.dev.technologia.androidapp.studybuddy.presentation.subject

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Subject
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Task
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.repository.SessionRepository
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.repository.SubjectRepository
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.repository.TaskRepository
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.navArgs
import com.developer.android.dev.technologia.androidapp.studybuddy.utils.SnackbarEvent
import com.developer.android.dev.technologia.androidapp.studybuddy.utils.toHours
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val taskRepository: TaskRepository,
    private val sessionRepository: SessionRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val navArgs: SubjectScreenNavArgs = savedStateHandle.navArgs()

    private val _state = MutableStateFlow(SubjectState())

    val state = combine(
        _state,
        taskRepository.getCompletedTasksForSubject(navArgs.subjectId),
        taskRepository.getUpcomingTasksForSubject(navArgs.subjectId),
        sessionRepository.getRecentTenSessionsForSubject(navArgs.subjectId),
        sessionRepository.getTotalSessionsDurationBySubject(navArgs.subjectId)
    ) { state,completedTask,upcomingTasks, recentSessions, totalSessionsDuration ->

        state.copy(
            upcomingTasks = upcomingTasks,
            completedTasks = completedTask,
            recentSession = recentSessions,
            studiedHours = totalSessionsDuration.toHours()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = SubjectState()
    )

    private val _snackBarEventFlow = MutableSharedFlow<SnackbarEvent>()
    val snackBarEventFlow = _snackBarEventFlow.asSharedFlow()

    init {
        fetchSubject()
    }

    fun onEvent(event: SubjectEvent) {
        when (event) {
            is SubjectEvent.OnGoalStudyHoursChange -> {
                _state.update {
                    it.copy(
                        goalStudyHours = event.hours
                    )
                }
            }

            is SubjectEvent.OnSubjectCardColorChange -> {
                _state.update {
                    it.copy(
                        subjectCardColors = event.color
                    )
                }
            }

            is SubjectEvent.OnSubjectNameChange -> {
                _state.update {
                    it.copy(
                        subjectName = event.name
                    )
                }
            }

            is SubjectEvent.OnTaskIsCompleteChange ->updateTask(event.task)
            SubjectEvent.UpdatedSubject -> updateSubject()
            SubjectEvent.DeletedSubject -> deleteSubject()
            SubjectEvent.DeletedSession -> deleteSession()
            is SubjectEvent.OnDeleteSessionsButtonClick -> {
                _state.update {
                    it.copy(session = event.session)
                }
            }
            SubjectEvent.UpdateProgress -> {
                val goalStudyHours = state.value.goalStudyHours.toFloatOrNull()?:1f
                _state.update {
                    it.copy(
                        progress = (state.value.studiedHours/goalStudyHours).coerceIn(0f,1f)
                    )
                }
            }
        }
    }

    private fun deleteSession(){
        viewModelScope.launch {
            try {
                state.value.session?.let { session->
                    sessionRepository.deleteSession(session=session)

                    _snackBarEventFlow.emit(
                        SnackbarEvent.ShowSnackBar(
                            "Session deleted successfully"
                        )
                    )
                }
            }catch (e:Exception){
                _snackBarEventFlow.emit(
                    SnackbarEvent.ShowSnackBar(
                        "Couldn't delete session. ${e.message}",
                        SnackbarDuration.Long
                    )
                )
            }
        }
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                taskRepository.upsertTask(
                    task=task.copy(
                        isComplete = !task.isComplete
                    )
                )
                if(task.isComplete){
                    _snackBarEventFlow.emit(
                        SnackbarEvent.ShowSnackBar(
                            "Saved in upcoming task"
                        )
                    )
                }else{
                    _snackBarEventFlow.emit(
                        SnackbarEvent.ShowSnackBar(
                            "Saved in completed task"
                        )
                    )
                }
            } catch (e: java.lang.Exception) {
                _snackBarEventFlow.emit(
                    SnackbarEvent.ShowSnackBar(
                        "Couldn't complete task. ${e.message}",
                        SnackbarDuration.Long
                    )
                )
            }

        }
    }

    private fun updateSubject() {
        viewModelScope.launch {
            try {
                subjectRepository.upsertSubject(
                    subject = Subject(
                        subjectId = state.value.currentSubjectId,
                        name = state.value.subjectName,
                        goalHours = state.value.goalStudyHours.toFloatOrNull() ?: 1f,
                        colors = state.value.subjectCardColors.map { it.toArgb() }
                    )
                )
                _snackBarEventFlow.emit(
                    SnackbarEvent.ShowSnackBar(
                        message = "Subject updated successfully."
                    )
                )
            } catch (e: Exception) {
                _snackBarEventFlow.emit(
                    SnackbarEvent.ShowSnackBar(
                        message = "Couldn't update subject. ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }

    private fun fetchSubject() {
        viewModelScope.launch {
            subjectRepository
                .getSubjectById(navArgs.subjectId)?.let { subject ->
                    _state.update {
                        it.copy(
                            subjectName = subject.name,
                            goalStudyHours = subject.goalHours.toString(),
                            subjectCardColors = subject.colors.map { colors -> Color(colors) },
                            currentSubjectId = subject.subjectId
                        )
                    }
                }
        }
    }

    private fun deleteSubject(){
        viewModelScope.launch {
            try {
                val currentSubjectId=state.value.currentSubjectId
                if(currentSubjectId!=null){
                    withContext(Dispatchers.IO){
                        subjectRepository.deleteSubject(subjectId = currentSubjectId)
                    }
                    _snackBarEventFlow.emit(
                        SnackbarEvent.ShowSnackBar(message = "Subject deleted successfully")
                    )
                    _snackBarEventFlow.emit(SnackbarEvent.NavigateUp)
                }else{
                    _snackBarEventFlow.emit(
                        SnackbarEvent.ShowSnackBar(message = "No Subject to delete")
                    )
                }

            }catch (e:Exception){
                _snackBarEventFlow.emit(
                    SnackbarEvent.ShowSnackBar(
                        message = "Couldn't delete subject. ${e.message}",
                        duration = SnackbarDuration.Long
                        )
                )
            }
        }
    }
}