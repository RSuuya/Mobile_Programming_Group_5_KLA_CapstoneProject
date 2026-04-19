package com.courseworktracker.viewmodel

import androidx.lifecycle.ViewModel
import com.courseworktracker.model.Coursework
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date

class CourseworkViewModel : ViewModel() {
    private val _courseworks = MutableStateFlow<List<Coursework>>(emptyList())
    val courseworks: StateFlow<List<Coursework>> = _courseworks.asStateFlow()

    init {
        // Sample data for initial setup
        _courseworks.value = listOf(
            Coursework("1", "Mobile App Development", "Programming", Date(), "Complete Phase 1 architecture"),
            Coursework("2", "Database Systems", "Computing", Date(), "Write SQL queries for Task 2")
        )
    }

    fun addCoursework(coursework: Coursework) {
        _courseworks.value = _courseworks.value + coursework
    }
}
