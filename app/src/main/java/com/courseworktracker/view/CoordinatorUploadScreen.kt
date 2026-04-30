package com.courseworktracker.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.courseworktracker.model.Assignment
import com.courseworktracker.viewmodel.AssignmentViewModel
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoordinatorUploadScreen(
    viewModel: AssignmentViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {}
) {
    var title by remember { mutableStateOf("") }
    var courseCode by remember { mutableStateOf("") }
    
    val assignments by viewModel.allAssignments.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TrackerTopAppBar(
                title = "Coordinator Panel",
                subtitle = "Upload Coursework",
                onLogout = onLogout
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
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
}
