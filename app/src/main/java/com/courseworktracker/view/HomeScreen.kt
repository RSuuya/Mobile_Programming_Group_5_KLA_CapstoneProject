package com.courseworktracker.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.courseworktracker.R
import com.courseworktracker.model.Assignment
import com.courseworktracker.ui.theme.NdejjeCourseworkTrackerTheme
import com.courseworktracker.viewmodel.AssignmentViewModel
import java.util.Date

@Composable
fun HomeScreen(
    viewModel: AssignmentViewModel,
    userName: String,
    isDarkMode: Boolean = false,
    onToggleDarkMode: () -> Unit = {},
    onAddAssignment: () -> Unit,
    onLogout: () -> Unit
) {
    val assignments by viewModel.allAssignments.collectAsState()
    
    HomeContent(
        assignments = assignments,
        userName = userName,
        onAddAssignment = onAddAssignment,
        onLogout = onLogout,
        onDeleteAssignment = { assignment -> viewModel.delete(assignment) },
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
    onDeleteAssignment: (Assignment) -> Unit = {},
    modifier: Modifier = Modifier,
    userName: String = "Student",
    onLogout: () -> Unit = {}
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Dashboard", "Archive")
    val icons = listOf(Icons.Filled.Dashboard, Icons.AutoMirrored.Filled.List)

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                items.forEachIndexed { index, item ->
                    val activeCount = assignments.count { !it.isCompleted }
                    NavigationBarItem(
                        icon = {
                            if (index == 0 && activeCount > 0) {
                                BadgedBox(
                                    badge = { Badge { Text("$activeCount") } }
                                ) {
                                    Icon(icons[index], contentDescription = item)
                                }
                            } else {
                                Icon(icons[index], contentDescription = item)
                            }
                        },
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
        }
    ) { innerPadding ->
        when (selectedItem) {
            0 -> {
                val activeAssignments = assignments.filter { !it.isCompleted }
                DashboardContent(
                    assignments = activeAssignments,
                    totalCount = assignments.size,
                    completedCount = assignments.count { it.isCompleted },
                    onAddAssignment = onAddAssignment,
                    onCompleteAssignment = onCompleteAssignment,
                    onDeleteAssignment = onDeleteAssignment,
                    userName = userName,
                    onLogout = onLogout,
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
            LargeTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ndejje_logo),
                                contentDescription = null,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Performance",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "Completed Coursework",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(assignments) { assignment ->
                    AssignmentCard(assignment)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    val sampleAssignments = listOf(
        Assignment(
            id = 1,
            title = "Mobile App Development",
            courseCode = "CS101",
            dueDate = Date(),
            isCompleted = false
        ),
        Assignment(
            id = 2,
            title = "Database Systems",
            courseCode = "CS102",
            dueDate = Date(),
            isCompleted = true
        ),
                Assignment(
                id = 3,
        title = "E-commerce",
        courseCode = "CS103",
        dueDate = Date(),
        isCompleted = true
    )
    )
    NdejjeCourseworkTrackerTheme(dynamicColor = false) {
        HomeContent(
            assignments = sampleAssignments,
            onAddAssignment = {},
            onCompleteAssignment = {}
        )
    }
}
