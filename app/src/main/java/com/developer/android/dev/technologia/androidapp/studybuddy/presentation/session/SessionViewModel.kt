package com.developer.android.dev.technologia.androidapp.studybuddy.presentation.session

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Session
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.repository.SessionRepository
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.repository.SubjectRepository
import com.developer.android.dev.technologia.androidapp.studybuddy.utils.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val subjectRepository: SubjectRepository
):ViewModel() {

    private val _state = MutableStateFlow(SessionState())
    val state = combine(
        _state,
        subjectRepository.getAllSubjects(),
        sessionRepository.getAllSessions()
    ){state,subjects,sessions->
        state.copy(
            subjects = subjects,
            sessions = sessions
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = SessionState()
    )

    private val _snackBarEventFlow = MutableSharedFlow<SnackbarEvent>()
    val snackBarEventFlow = _snackBarEventFlow.asSharedFlow()

    fun onEvent(event: SessionEvent){
        when(event){
            SessionEvent.NotifyToUpdateSubject -> notifyToUpdateSubject()
            SessionEvent.DeleteSession -> deleteSession()
            is SessionEvent.OnDeleteSessionButtonClick -> {
                _state.update {
                    it.copy(session = event.session)
                }
            }
            is SessionEvent.OnRelatedSubjectChange -> {
                _state.update {
                    it.copy(
                        relatedToSubject = event.subject.name,
                        subjectId = event.subject.subjectId
                    )
                }
            }
            is SessionEvent.SaveSession -> insertSession(event.duration)
            is SessionEvent.UpdateSubjectRelatedSubject -> {
                _state.update {
                    it.copy(
                        relatedToSubject = event.relatedToSubject,
                        subjectId = event.subjectId
                    )
                }
            }
        }
    }

    private fun notifyToUpdateSubject() {
        viewModelScope.launch {
            if(state.value.subjectId == null || state.value.relatedToSubject ==null){
                _snackBarEventFlow.emit(
                    SnackbarEvent.ShowSnackBar(
                        message = "Please select subject related to session.",
                        duration = SnackbarDuration.Long
                    )
                )
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
    private fun insertSession(duration: Long) {
        viewModelScope.launch {
            if(duration<10){
                _snackBarEventFlow.emit(
                    SnackbarEvent.ShowSnackBar(
                        "Single session can not be less tha 59 seconds"
                    )
                )
                return@launch
            }
           try {
               sessionRepository.insertSession(
                   session = Session(
                       sessionSubjectId = state.value.subjectId?:-1,
                       relatedToSubject = state.value.relatedToSubject?:"",
                       date = Instant.now().toEpochMilli(),
                       duration = duration
                   )
               )
               _snackBarEventFlow.emit(
                   SnackbarEvent.ShowSnackBar(
                       "Session saved successfully"
                   )
               )
           }catch (e:Exception){
               _snackBarEventFlow.emit(
                   SnackbarEvent.ShowSnackBar(
                       "Couldn't save session. ${e.message}",
                       SnackbarDuration.Long
                   )
               )
           }
        }
    }
}