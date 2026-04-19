package com.courseworktracker.repository

import com.courseworktracker.model.Coursework
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*

class CourseworkRepository {
    // Local list to simulate database before Room implementation
    private val _courseworkList = MutableStateFlow<List<Coursework>>(
        listOf(
            Coursework(UUID.randomUUID().toString(), "Phase 1: Architecture", "Mobile Development", Date(), "Setting up project folders"),
            Coursework(UUID.randomUUID().toString(), "Phase 2: UI Design", "Mobile Development", Date(), "Building screens with Compose")
        )
    )
    val courseworkList = _courseworkList.asStateFlow()

    fun getAllCoursework(): Flow<List<Coursework>> = courseworkList

    fun addCoursework(coursework: Coursework) {
        val current = _courseworkList.value.toMutableList()
        current.add(coursework)
        _courseworkList.value = current
    }

    fun removeCoursework(id: String) {
        _courseworkList.value = _courseworkList.value.filter { it.id != id }
    }
}
