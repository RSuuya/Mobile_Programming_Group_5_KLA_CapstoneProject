package com.courseworktracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.courseworktracker.model.Assignment
import com.courseworktracker.repository.AssignmentRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AssignmentViewModel(private val repository: AssignmentRepository) : ViewModel() {
    val allAssignments: StateFlow<List<Assignment>> = repository.allAssignments
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insert(assignment: Assignment) = viewModelScope.launch {
        repository.insert(assignment)
    }

    fun update(assignment: Assignment) = viewModelScope.launch {
        repository.update(assignment)
    }

    fun delete(assignment: Assignment) = viewModelScope.launch {
        repository.delete(assignment)
    }
}
