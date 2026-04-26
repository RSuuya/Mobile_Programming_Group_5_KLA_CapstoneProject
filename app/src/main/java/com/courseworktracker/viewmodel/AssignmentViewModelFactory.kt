package com.courseworktracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.courseworktracker.repository.AssignmentRepository

class AssignmentViewModelFactory(private val repository: AssignmentRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AssignmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AssignmentViewModel(
                repository,
                userPreferencesRepository = TODO()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
