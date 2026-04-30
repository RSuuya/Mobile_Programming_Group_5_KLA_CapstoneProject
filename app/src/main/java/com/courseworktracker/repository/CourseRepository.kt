package com.courseworktracker.repository

import com.courseworktracker.model.Course
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CourseRepository @Inject constructor(
    private val courseDao: CourseDao
) {
    val allCourses: Flow<List<Course>> = courseDao.getAllCourses()

    suspend fun insert(course: Course) {
        courseDao.upsertCourse(course)
    }

    suspend fun delete(course: Course) {
        courseDao.deleteCourse(course)
    }
}
