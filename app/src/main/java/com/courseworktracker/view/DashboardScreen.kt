package com.courseworktracker.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.courseworktracker.R
import com.courseworktracker.model.Assignment
import com.courseworktracker.ui.theme.NdejjeCourseworkTrackerTheme
import com.courseworktracker.viewmodel.AssignmentViewModel
import java.util.Date

@Composable
fun DashboardScreen(
    viewModel: AssignmentViewModel,
    onAddAssignment: () -> Unit
) {
    val assignments by viewModel.allAssignments.collectAsState()
    val activeAssignments = assignments.filter { !it.isCompleted }

    DashboardContent(
        assignments = activeAssignments,
        onAddAssignment = onAddAssignment,
        onCompleteAssignment = { assignment ->
            viewModel.update(assignment.copy(isCompleted = true))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardContent(
    assignments: List<Assignment>,
    onAddAssignment: () -> Unit,
    onCompleteAssignment: (Assignment) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.home_title)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddAssignment) {
                Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.add_coursework))
            }
        }
    ) { innerPadding ->
        if (assignments.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(id = R.string.no_assignments))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(dimensionResource(id = R.dimen.padding_medium))
            ) {
                item {
                    Text(
                        text = stringResource(id = R.string.upcoming_deadlines),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_small))
                    )
                }
                items(assignments) { assignment ->
                    AssignmentCard(
                        assignment = assignment,
                        onComplete = { onCompleteAssignment(assignment) }
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer_height)))
                }
            }
        }
    }
}

@Composable
fun AssignmentCard(
    assignment: Assignment,
    onComplete: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(dimensionResource(id = R.dimen.card_elevation))
    ) {
        Row(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_medium))
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = assignment.title, style = MaterialTheme.typography.headlineSmall)
                Text(text = assignment.courseCode, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = stringResource(id = R.string.due_prefix) + assignment.dueDate.toString(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            if (onComplete != null && !assignment.isCompleted) {
                IconButton(onClick = onComplete) {
                    Icon(Icons.Default.Check, contentDescription = "Complete")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    val sampleAssignments = listOf(
        Assignment(1, "Mobile App Development", "CS101", Date(), false),
        Assignment(2, "Database Systems", "CS102", Date(), false)
    )
    NdejjeCourseworkTrackerTheme(dynamicColor = false) {
        DashboardContent(
            assignments = sampleAssignments,
            onAddAssignment = {},
            onCompleteAssignment = {}
        )
    }
}
