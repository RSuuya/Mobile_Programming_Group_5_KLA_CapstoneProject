package com.courseworktracker.repository

import com.courseworktracker.model.Assignment
import kotlinx.coroutines.flow.Flow

class AssignmentRepository(private val assignmentDao: AssignmentDao) {
    val allAssignments: Flow<List<Assignment>> = assignmentDao.getAllAssignments()

    suspend fun insert(assignment: Assignment) {
        assignmentDao.insertAssignment(assignment)
    }

    suspend fun update(assignment: Assignment) {
        assignmentDao.updateAssignment(assignment)
    }

    suspend fun delete(assignment: Assignment) {
        assignmentDao.deleteAssignment(assignment)
    }
}
