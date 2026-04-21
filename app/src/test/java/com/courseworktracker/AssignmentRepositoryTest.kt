package com.courseworktracker

import com.courseworktracker.model.Assignment
import com.courseworktracker.repository.AssignmentDao
import com.courseworktracker.repository.AssignmentRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.Date

class AssignmentRepositoryTest {

    @Mock
    private lateinit var assignmentDao: AssignmentDao
    private lateinit var repository: AssignmentRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = AssignmentRepository(assignmentDao)
    }

    @Test
    fun getAllAssignments_returnsFlowFromDao() = runBlocking {
        val assignments = listOf(Assignment(1, "Test", "CS101", Date(), false))
        whenever(assignmentDao.getAllAssignments()).thenReturn(flowOf(assignments))

        val result = repository.allAssignments.first()
        assertEquals(assignments, result)
    }

    @Test
    fun insert_callsDaoInsert() = runBlocking {
        val assignment = Assignment(1, "Test", "CS101", Date(), false)
        repository.insert(assignment)
        verify(assignmentDao).insertAssignment(assignment)
    }
}
