package com.courseworktracker.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.courseworktracker.model.Assignment
import com.courseworktracker.viewmodel.AssignmentViewModel
import java.util.*

@Composable
fun CoordinatorDashboardScreen(
    viewModel: AssignmentViewModel,
    onLogout: () -> Unit = {}
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Broadcast", "Manage")
    val icons = listOf(Icons.Default.FileUpload, Icons.Default.People)

    val assignments by viewModel.allAssignments.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TrackerTopAppBar(
                title = if (selectedItem == 0) "Coordinator Panel" else "Manage Tasks",
                subtitle = if (selectedItem == 0) "Broadcast Assignments" else "View All Submissions",
                onLogout = onLogout
            )
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
        }
    ) { innerPadding ->
        when (selectedItem) {
            0 -> BroadcastBody(
                viewModel = viewModel,
                assignments = assignments,
                modifier = Modifier.padding(innerPadding)
            )
            1 -> ManageTasksBody(
                assignments = assignments,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun BroadcastBody(
    viewModel: AssignmentViewModel,
    assignments: List<Assignment>,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }
    var courseCode by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Broadcast New Assignment",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Assignment Title") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = courseCode,
                    onValueChange = { courseCode = it },
                    label = { Text("Course Code (e.g., BCS2201)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = {
                        if (title.isNotBlank() && courseCode.isNotBlank()) {
                            viewModel.insert(
                                Assignment(
                                    title = title,
                                    courseCode = courseCode,
                                    dueDate = Date(),
                                    isFromCoordinator = true
                                )
                            )
                            title = ""
                            courseCode = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.FileUpload, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Upload for all Students")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Recently Uploaded", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(assignments.filter { it.isFromCoordinator }.reversed()) { assignment ->
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(assignment.title, fontWeight = FontWeight.Bold)
                            Text(assignment.courseCode, style = MaterialTheme.typography.bodySmall)
                        }
                        Text("Official", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ManageTasksBody(
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
                    text = "No assignments managed yet",
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
