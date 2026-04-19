package com.courseworktracker.model

import java.util.Date

data class Coursework(
    val id: String,
    val name: String,
    val subject: String,
    val dueDate: Date,
    val description: String,
    val isCompleted: Boolean = false
)
