package com.courseworktracker.repository

import androidx.room.*
import com.courseworktracker.model.Course
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {
    @Query("SELECT * FROM courses ORDER BY name ASC")
    fun getAllCourses(): Flow<List<Course>>

    @Upsert
    suspend fun upsertCourse(course: Course): Long

    @Delete
    suspend fun deleteCourse(course: Course): Int
}
