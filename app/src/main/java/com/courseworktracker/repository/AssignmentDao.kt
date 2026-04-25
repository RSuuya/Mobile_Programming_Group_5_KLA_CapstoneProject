package com.courseworktracker.repository

import androidx.room.*
import com.courseworktracker.model.Assignment
import kotlinx.coroutines.flow.Flow

@Dao
interface AssignmentDao {
    // 1. Dashboard View: Fetch incomplete assignments sorted by the nearest deadline
    @Query("SELECT * FROM assignments WHERE isCompleted = 0 ORDER BY dueDate ASC")
    fun getUpcomingAssignments(): Flow<List<Assignment>>

    // 2. Performance Archive View: Fetch completed assignments
    @Query("SELECT * FROM assignments WHERE isCompleted = 1 ORDER BY dueDate DESC")
    fun getCompletedAssignments(): Flow<List<Assignment>>

    @Query("SELECT * FROM assignments ORDER BY dueDate ASC")
    fun getAllAssignments(): Flow<List<Assignment>>

    @Upsert
    suspend fun upsertAssignment(assignment: Assignment): Long

    @Update
    suspend fun updateAssignment(assignment: Assignment): Int

    @Delete
    suspend fun deleteAssignment(assignment: Assignment): Int
}
