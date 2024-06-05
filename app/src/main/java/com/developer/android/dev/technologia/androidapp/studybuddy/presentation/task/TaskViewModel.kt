package com.developer.android.dev.technologia.androidapp.studybuddy.presentation.task

import androidx.lifecycle.ViewModel
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
):ViewModel() {
}