package com.courseworktracker.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun HomeScreen(
    viewModel: AssignmentViewModel,
    onAddAssignment: () -> Unit
) {
    val assignments by viewModel.allAssignments.collectAsState()
    
    HomeContent(
        assignments = assignments,
        onAddAssignment = onAddAssignment,
        onCompleteAssignment = { assignment ->
            viewModel.update(assignment.copy(isCompleted = true))
        }
    )
}

@Composable
fun HomeContent(
    assignments: List<Assignment>,
    onAddAssignment: () -> Unit,
    onCompleteAssignment: (Assignment) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Dashboard", "Archive")
    val icons = listOf(Icons.Filled.Home, Icons.AutoMirrored.Filled.List)

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddAssignment) {
                Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.add_coursework))
            }
        }
    ) { innerPadding ->
        when (selectedItem) {
            0 -> {
                val activeAssignments = assignments.filter { !it.isCompleted }
                DashboardContent(
                    assignments = activeAssignments,
                    onAddAssignment = onAddAssignment,
                    onCompleteAssignment = onCompleteAssignment,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            1 -> {
                val archivedAssignments = assignments.filter { it.isCompleted }
                ArchiveContent(
                    assignments = archivedAssignments,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchiveContent(
    assignments: List<Assignment>,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.archive_title)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { innerPadding ->
        if (assignments.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No archived assignments")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(dimensionResource(id = R.dimen.padding_medium))
            ) {
                items(assignments) { assignment ->
                    AssignmentCard(assignment)
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer_height)))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    val sampleAssignments = listOf(
        Assignment(1, "Mobile App Development", "CS101", Date(), false),
        Assignment(2, "Database Systems", "CS102", Date(), true)
    )
    NdejjeCourseworkTrackerTheme(dynamicColor = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HomeContent(
                assignments = sampleAssignments,
                onAddAssignment = {},
                onCompleteAssignment = {}
            )
        }
    }
}
