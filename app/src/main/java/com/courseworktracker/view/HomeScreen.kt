package com.courseworktracker.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.courseworktracker.model.Assignment
import com.courseworktracker.viewmodel.AssignmentViewModel

@Composable
fun HomeScreen(
    viewModel: AssignmentViewModel,
    userName: String = "Student",
    onAddAssignment: () -> Unit,
    isCoordinator: Boolean = false,
    onLogout: () -> Unit = {}
) {
    val assignments by viewModel.allAssignments.collectAsState()
    
    HomeContent(
        assignments = assignments,
        viewModel = viewModel,
        userName = userName,
        onAddAssignment = onAddAssignment,
        onLogout = onLogout,
        isCoordinator = isCoordinator,
        onCompleteAssignment = { assignment ->
            viewModel.update(assignment.copy(isCompleted = true))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    assignments: List<Assignment>,
    viewModel: AssignmentViewModel,
    userName: String,
    onAddAssignment: () -> Unit,
    onCompleteAssignment: (Assignment) -> Unit,
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {},
    isCoordinator: Boolean = false
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    
    val items = if (isCoordinator) {
        listOf("Dashboard", "Archive", "Coordinator")
    } else {
        listOf("Dashboard", "Archive")
    }
    
    val icons = if (isCoordinator) {
        listOf(Icons.Filled.Dashboard, Icons.AutoMirrored.Filled.List, Icons.Default.FileUpload)
    } else {
        listOf(Icons.Filled.Dashboard, Icons.AutoMirrored.Filled.List)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column {
                TrackerTopAppBar(
                    title = when (selectedItem) {
                        0 -> "Ndejje Tracker"
                        1 -> "Performance"
                        else -> "Coordinator Panel"
                    },
                    subtitle = when (selectedItem) {
                        0 -> "Faculty of Computing"
                        1 -> "Completed Coursework"
                        else -> "Broadcast Assignments"
                    },
                    userName = userName,
                    onLogout = onLogout
                )
                if (selectedItem == 0) {
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        onSearch = {},
                        active = false,
                        onActiveChange = {},
                        placeholder = { Text("Search assignments or codes...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {}
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item, fontWeight = FontWeight.Medium) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        },
        floatingActionButton = {
            if (selectedItem == 0) {
                ExtendedFloatingActionButton(
                    onClick = onAddAssignment,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(16.dp),
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("New Coursework") }
                )
            }
        }
    ) { innerPadding ->
        val filteredAssignments = assignments.filter { 
            it.title.contains(searchQuery, ignoreCase = true) || 
            it.courseCode.contains(searchQuery, ignoreCase = true)
        }

        when (selectedItem) {
            0 -> {
                val activeAssignments = filteredAssignments.filter { !it.isCompleted }
                DashboardBody(
                    assignments = activeAssignments,
                    totalCount = assignments.size,
                    completedCount = assignments.count { it.isCompleted },
                    onCompleteAssignment = onCompleteAssignment,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            1 -> {
                val archivedAssignments = filteredAssignments.filter { it.isCompleted }
                ArchiveBody(
                    assignments = archivedAssignments,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            2 -> {
                BroadcastBody(
                    viewModel = viewModel,
                    assignments = assignments,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun ArchiveBody(
    assignments: List<Assignment>,
    modifier: Modifier = Modifier
) {
    if (assignments.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.AutoMirrored.Filled.Assignment,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No archived tasks",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray
                )
            }
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier.fillMaxSize()
        ) {
            items(assignments) { assignment ->
                AssignmentCard(assignment)
            }
        }
    }
}
