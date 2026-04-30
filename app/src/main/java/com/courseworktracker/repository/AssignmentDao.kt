package com.courseworktracker.repository

import androidx.room.*
import com.courseworktracker.model.Assignment
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface AssignmentDao {
    @Query("SELECT * FROM assignments WHERE isCompleted = 0 ORDER BY dueDate ASC")
    fun getUpcomingAssignments(): Flow<List<Assignment>>

    @Query("SELECT * FROM assignments WHERE isCompleted = 1 ORDER BY dueDate DESC")
    fun getCompletedAssignments(): Flow<List<Assignment>>

    @Query("SELECT * FROM assignments ORDER BY dueDate ASC")
    fun getAllAssignments(): Flow<List<Assignment>>

    // For Worker: Fetch assignments due in the next 24 hours synchronously
    @Query("SELECT * FROM assignments WHERE isCompleted = 0 AND dueDate > :now AND dueDate <= :tomorrow")
    suspend fun getAssignmentsDueSoon(now: Date, tomorrow: Date): List<Assignment>

    @Upsert
    suspend fun upsertAssignment(assignment: Assignment): Long

    @Update
    suspend fun updateAssignment(assignment: Assignment): Int

    @Delete
    suspend fun deleteAssignment(assignment: Assignment): Int
}
