package com.courseworktracker.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.courseworktracker.R
import com.courseworktracker.model.Assignment
import com.courseworktracker.ui.theme.NdejjeCourseworkTrackerTheme
import com.courseworktracker.viewmodel.AssignmentViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem

enum class AssignmentFilter { ALL, OVERDUE, UPCOMING }
@Composable
fun DashboardScreen(
    viewModel: AssignmentViewModel,
    onAddAssignment: () -> Unit,
    onLogout: () -> Unit = {},
    userName: String = "Student"
) {
    val assignments by viewModel.allAssignments.collectAsState(initial = emptyList())
    val activeAssignments = assignments.filter { !it.isCompleted }
    val totalCount = assignments.size
    val completedCount = assignments.count { it.isCompleted }

    DashboardContent(
        assignments = activeAssignments,
        totalCount = totalCount,
        completedCount = completedCount,
        onAddAssignment = onAddAssignment,
        onCompleteAssignment = { assignment ->
            viewModel.update(assignment.copy(isCompleted = true))
        },
        onLogout = onLogout,
        userName = userName
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackerTopAppBar(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    userName: String = "User",
    onLogout: () -> Unit = {},
    showLogout: Boolean = true,
    hasNewCoordinatorTask: Boolean = false,
    selectedFilter: AssignmentFilter = AssignmentFilter.ALL,
    onFilterSelected: (AssignmentFilter) -> Unit = {},
    isDarkMode: Boolean = false,           // ✅ add
    onToggleDarkMode: () -> Unit = {}
) {
    LargeTopAppBar(
        modifier = modifier,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    modifier = Modifier.size(52.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ndejje_logo),
                        contentDescription = null,
                        modifier = Modifier.padding(6.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = userName,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
            }
        },
        actions = {
            //   this block contains actions
            var expanded by remember { mutableStateOf(false) }
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        Icons.Default.FilterList,
                        contentDescription = "Filter",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                "All",
                                fontWeight = if (selectedFilter == AssignmentFilter.ALL)
                                    FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        onClick = {
                            onFilterSelected(AssignmentFilter.ALL)
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                "Overdue",
                                fontWeight = if (selectedFilter == AssignmentFilter.OVERDUE)
                                    FontWeight.Bold else FontWeight.Normal,
                                color = MaterialTheme.colorScheme.error
                            )
                        },
                        onClick = {
                            onFilterSelected(AssignmentFilter.OVERDUE)
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                "Upcoming",
                                fontWeight = if (selectedFilter == AssignmentFilter.UPCOMING)
                                    FontWeight.Bold else FontWeight.Normal,
                                color = MaterialTheme.colorScheme.primary
                            )
                        },
                        onClick = {
                            onFilterSelected(AssignmentFilter.UPCOMING)
                            expanded = false
                        }
                    )
                }
            }
            if (hasNewCoordinatorTask) {
                BadgedBox(
                    badge = { Badge { Text("!") } },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = "New official task",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            // ✅ add dark mode toggle here
            IconButton(onClick = onToggleDarkMode) {
                Icon(
                    imageVector = if (isDarkMode) Icons.Default.LightMode
                    else Icons.Default.DarkMode,
                    contentDescription = if (isDarkMode) "Switch to light mode"
                    else "Switch to dark mode",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            if (showLogout) {
                IconButton(onClick = onLogout) {
                    Icon(
                        Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Logout",
                        tint = MaterialTheme.colorScheme.primary
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

@Composable
fun DashboardContent(
    assignments: List<Assignment>,
    totalCount: Int,
    completedCount: Int,
    onAddAssignment: () -> Unit,
    onCompleteAssignment: (Assignment) -> Unit,
    onDeleteAssignment: (Assignment) -> Unit = {},
    onEditAssignment: (Assignment) -> Unit = {},
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {},
    userName: String = "User",
    isDarkMode: Boolean = false,
    onToggleDarkMode: () -> Unit = {}
) {
    val listState = rememberLazyListState()
    val isExpanded by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }
    var selectedFilter by remember { mutableStateOf(AssignmentFilter.ALL) }

    //   apply filter to assignments before passing to body
    val filteredAssignments = when (selectedFilter) {
        AssignmentFilter.ALL -> assignments
        AssignmentFilter.OVERDUE -> assignments.filter {
            it.dueDate.time < System.currentTimeMillis()
        }
        AssignmentFilter.UPCOMING -> assignments.filter {
            it.dueDate.time >= System.currentTimeMillis()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TrackerTopAppBar(
                title = "Ndejje Tracker",
                subtitle = "Faculty of Computing",
                userName = userName,
                onLogout = onLogout,
                hasNewCoordinatorTask = assignments.any { it.isFromCoordinator && !it.isCompleted },
                isDarkMode = isDarkMode,
                onToggleDarkMode = onToggleDarkMode,
                        selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it },

            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddAssignment,
                expanded = isExpanded,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp),
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("New Coursework") }
            )
        }
    ) { innerPadding ->
        DashboardBody(
            assignments = filteredAssignments,
            totalCount = totalCount,
            completedCount = completedCount,
            onCompleteAssignment = onCompleteAssignment,
            onDeleteAssignment = onDeleteAssignment,
            onEditAssignment = onEditAssignment,
            listState = listState,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun DashboardBody(
    assignments: List<Assignment>,
    totalCount: Int,
    completedCount: Int,
    onCompleteAssignment: (Assignment) -> Unit,
    onDeleteAssignment: (Assignment) -> Unit = {},
    onEditAssignment: (Assignment) -> Unit = {},
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState()
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        ProgressOverviewCard(totalCount = totalCount, completedCount = completedCount)
        
        if (assignments.isEmpty()) {
            EmptyDashboardState()
        } else {
            val grouped = assignments.groupBy { 
                val diff = it.dueDate.time - System.currentTimeMillis()
                val days = TimeUnit.MILLISECONDS.toDays(diff)
                when {
                    diff < 0 -> "Overdue"
                    days < 1 -> "Due Today"
                    days < 3 -> "Upcoming"
                    else -> "Later"
                }
            }

            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                listOf("Overdue", "Due Today", "Upcoming", "Later").forEach { category ->
                    grouped[category]?.let { categoryAssignments ->
                        item {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = when(category) {
                                    "Overdue" -> MaterialTheme.colorScheme.error
                                    "Due Today" -> Color(0xFFFB8C00)
                                    else -> MaterialTheme.colorScheme.primary
                                },
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(categoryAssignments) { assignment ->
                            AssignmentCard(
                                assignment = assignment,
                                onComplete = { onCompleteAssignment(assignment) },
                                onDelete = { onDeleteAssignment(assignment) },
                                onEdit = { onEditAssignment(assignment) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProgressOverviewCard(totalCount: Int, completedCount: Int) {
    val progress = if (totalCount > 0) completedCount.toFloat() / totalCount.toFloat() else 0f
    
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Your Progress",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = "$completedCount of $totalCount completed",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                    )
                }
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(CircleShape),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.1f)
            )
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
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentCard(
    assignment: Assignment,
    onComplete: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
    onEdit: (() -> Unit)? = null
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete?.invoke()
                true
            } else false
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,   // only left swipe
        backgroundContent = {
            // Red background revealed on swipe
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.errorContainer),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(
                    modifier = Modifier.padding(end = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Delete",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
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
                            painterResource(id = android.R.drawable.ic_menu_myplaces),
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = if (deadlineColor == MaterialTheme.colorScheme.error)
                                MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text(
                                text = dateFormat.format(assignment.dueDate),
                                style = MaterialTheme.typography.bodySmall,
                                color = if (deadlineColor == MaterialTheme.colorScheme.error)
                                    MaterialTheme.colorScheme.error
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (!assignment.isCompleted) {
                                CountdownTimer(assignment.dueDate)
                            }
                        }
                    }
                }
                if (onEdit != null && !assignment.isCompleted) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f))
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
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
}@Composable
fun CountdownTimer(dueDate: Date) {
    var timeLeft by remember { mutableLongStateOf(dueDate.time - System.currentTimeMillis()) }

    LaunchedEffect(key1 = timeLeft) {
        if (timeLeft > 0) {
            delay(1000)
            timeLeft = dueDate.time - System.currentTimeMillis()
        }
    }

    if (timeLeft > 0) {
        val days = TimeUnit.MILLISECONDS.toDays(timeLeft)
        val hours = TimeUnit.MILLISECONDS.toHours(timeLeft) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeft) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeft) % 60

        Text(
            text = String.format(Locale.getDefault(), "%dd %02dh %02dm %02ds left", days, hours, minutes, seconds),
            style = MaterialTheme.typography.labelSmall,
            color = if (days < 1) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
    } else {
        Text(
            text = "Overdue",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun getDeadlineColor(dueDate: Date): Color {
    val diff = dueDate.time - System.currentTimeMillis()
    val days = TimeUnit.MILLISECONDS.toDays(diff)
    return when {
        diff < 0 -> MaterialTheme.colorScheme.outline  // Overdue
        days < 1 -> MaterialTheme.colorScheme.error    // Due today = RED ✅
        days < 3 -> Color(0xFFFB8C00)                  // Within 3 days = Orange ✅
        else -> Color(0xFF43A047)                       // Later = Green
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    val sampleAssignments = listOf(
        Assignment(
            id = 1,
            title = "Mobile App Project",
            courseCode = "BCS2201",
            dueDate = Date(System.currentTimeMillis() + 3600000),
            isCompleted = false
        ),
        Assignment(
            id = 2,
            title = "Database Systems",
            courseCode = "BIT2205",
            dueDate = Date(System.currentTimeMillis() + 172800000),
            isCompleted = true
        )
    )
    NdejjeCourseworkTrackerTheme(dynamicColor = false) {
        DashboardContent(
            assignments = sampleAssignments.filter { !it.isCompleted },
            totalCount = 2,
            completedCount = 1,
            onAddAssignment = {},
            onCompleteAssignment = {}
        )
    }
}
