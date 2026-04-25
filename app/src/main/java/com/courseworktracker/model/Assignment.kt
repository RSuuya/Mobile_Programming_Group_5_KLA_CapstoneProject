package com.courseworktracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "assignments")
data class Assignment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val courseCode: String,
    val lecturer: String = "",
    val dueDate: Date,
    val isCompleted: Boolean = false,
    val priorityColor: String = "#4CAF50", // Default Green
    val isFromCoordinator: Boolean = false
)
