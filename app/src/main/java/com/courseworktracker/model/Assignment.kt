package com.courseworktracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

enum class Priority {
    LOW, MEDIUM, HIGH
}

@Entity(tableName = "assignments")
data class Assignment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val courseCode: String,
    val lecturer: String = "",
    val dueDate: Date,
    val notes: String = "",
    val isCompleted: Boolean = false,
    val priority: Priority = Priority.MEDIUM,
    val isFromCoordinator: Boolean = false
)
