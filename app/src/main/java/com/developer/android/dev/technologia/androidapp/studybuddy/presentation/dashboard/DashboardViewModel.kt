package com.developer.android.dev.technologia.androidapp.studybuddy.presentation.dashboard

import androidx.lifecycle.ViewModel
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository
):ViewModel(){

}