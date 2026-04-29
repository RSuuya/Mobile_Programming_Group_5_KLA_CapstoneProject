package com.courseworktracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.courseworktracker.data.UserPreferences
import com.courseworktracker.data.UserPreferencesRepository
import com.courseworktracker.model.Assignment
import com.courseworktracker.repository.AssignmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class CourseworkFilter { ALL, PENDING, COMPLETED }

@HiltViewModel
class AssignmentViewModel @Inject constructor(
    private val repository: AssignmentRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val searchQuery = MutableStateFlow("")
    val courseworkFilter = MutableStateFlow(CourseworkFilter.ALL)

    val allAssignments: StateFlow<List<Assignment>> = repository.allAssignments
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val filteredAssignments: StateFlow<List<Assignment>> = combine(
        allAssignments,
        searchQuery,
        courseworkFilter
    ) { assignments, query, filter ->
        assignments.filter { assignment ->
            val matchesQuery = assignment.title.contains(query, ignoreCase = true) ||
                    assignment.courseCode.contains(query, ignoreCase = true)
            val matchesFilter = when (filter) {
                CourseworkFilter.ALL -> true
                CourseworkFilter.PENDING -> !assignment.isCompleted
                CourseworkFilter.COMPLETED -> assignment.isCompleted
            }
            matchesQuery && matchesFilter
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val completionProgress: StateFlow<Float> = allAssignments.map { assignments ->
        if (assignments.isEmpty()) 0f
        else {
            val completed = assignments.count { it.isCompleted }
            completed.toFloat() / assignments.size
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0f
    )

    val userPreferences: StateFlow<UserPreferences> = userPreferencesRepository.userPreferencesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserPreferences("Student", isCoordinator = false, isLoggedIn = false)
        )

    fun onSearchQueryChange(newQuery: String) {
        searchQuery.value = newQuery
    }

    fun onFilterChange(newFilter: CourseworkFilter) {
        courseworkFilter.value = newFilter
    }

    fun updateLoginState(userName: String, isCoordinator: Boolean, isLoggedIn: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateLoginState(userName, isCoordinator, isLoggedIn)
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPreferencesRepository.clear()
        }
    }

    fun toggleDarkMode() {
        viewModelScope.launch {
            val current = userPreferences.value.isDarkMode
            userPreferencesRepository.toggleDarkMode(!current)
        }
    }
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