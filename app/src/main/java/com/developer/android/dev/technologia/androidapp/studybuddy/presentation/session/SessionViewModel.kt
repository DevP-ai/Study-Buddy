package com.developer.android.dev.technologia.androidapp.studybuddy.presentation.session

import androidx.lifecycle.ViewModel
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
):ViewModel() {

}