package com.courseworktracker.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.courseworktracker.R
import com.courseworktracker.model.Assignment
import com.courseworktracker.ui.theme.NdejjeCourseworkTrackerTheme
import com.courseworktracker.viewmodel.AssignmentViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

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
                                text = "Ndejje Tracker",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "Faculty of Computing",
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
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddAssignment,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp),
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("New Coursework") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (assignments.isEmpty()) {
                EmptyDashboardState()
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Text(
                            text = stringResource(id = R.string.upcoming_deadlines),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    items(assignments) { assignment ->
                        AssignmentCard(
                            assignment = assignment,
                            onComplete = { onCompleteAssignment(assignment) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyDashboardState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.Assignment,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "All caught up!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "No pending coursework for now.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun AssignmentCard(
    assignment: Assignment,
    onComplete: (() -> Unit)? = null
) {
    val deadlineColor = getDeadlineColor(assignment.dueDate)
    val dateFormat = SimpleDateFormat("EEE, MMM dd 'at' HH:mm", Locale.getDefault())

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Priority Bar
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(60.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(deadlineColor)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = assignment.courseCode,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = assignment.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painterResource(id = android.R.drawable.ic_menu_myplaces), // Using a system icon for simplicity
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = if (deadlineColor == Color.Red) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = dateFormat.format(assignment.dueDate),
                        style = MaterialTheme.typography.bodySmall,
                        color = if (deadlineColor == Color.Red) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (onComplete != null && !assignment.isCompleted) {
                IconButton(
                    onClick = onComplete,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Complete",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

private fun getDeadlineColor(dueDate: Date): Color {
    val diff = dueDate.time - System.currentTimeMillis()
    val days = TimeUnit.MILLISECONDS.toDays(diff)
    return when {
        diff < 0 -> Color.Gray
        days < 1 -> Color(0xFFE53935) // Deep Red
        days < 3 -> Color(0xFFFB8C00) // Deep Orange
        else -> Color(0xFF43A047) // Dark Green
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    val sampleAssignments = listOf(
        Assignment(1, "Mobile App Project", "BCS2201", Date(System.currentTimeMillis() + 3600000), false),
        Assignment(2, "Database Systems", "BIT2205", Date(System.currentTimeMillis() + 172800000), false)
    )
    NdejjeCourseworkTrackerTheme(dynamicColor = false) {
        DashboardContent(
            assignments = sampleAssignments,
            onAddAssignment = {},
            onCompleteAssignment = {}
        )
    }
}
