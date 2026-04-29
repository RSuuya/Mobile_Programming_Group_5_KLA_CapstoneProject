package com.courseworktracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.courseworktracker.data.UserPreferencesRepository
import com.courseworktracker.repository.AssignmentRepository
import com.courseworktracker.repository.CourseRepository

class AssignmentViewModelFactory(
    private val repository: AssignmentRepository,
    private val courseRepository: CourseRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AssignmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AssignmentViewModel(
                repository,
                courseRepository,
                userPreferencesRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
