package com.courseworktracker.repository

import com.courseworktracker.model.Assignment
import kotlinx.coroutines.flow.Flow

class AssignmentRepository(private val assignmentDao: AssignmentDao) {
    val allAssignments: Flow<List<Assignment>> = assignmentDao.getAllAssignments()
    val upcomingAssignments: Flow<List<Assignment>> = assignmentDao.getUpcomingAssignments()
    val completedAssignments: Flow<List<Assignment>> = assignmentDao.getCompletedAssignments()

    suspend fun insert(assignment: Assignment) {
        assignmentDao.upsertAssignment(assignment)
    }

    suspend fun update(assignment: Assignment) {
        assignmentDao.updateAssignment(assignment)
    }

    suspend fun delete(assignment: Assignment) {
        assignmentDao.deleteAssignment(assignment)
    }
}
