package com.courseworktracker.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.courseworktracker.R
import com.courseworktracker.model.Coursework
import com.courseworktracker.viewmodel.CourseworkViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: CourseworkViewModel = viewModel()) {
    val courseworks by viewModel.courseworks.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.home_title)) }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            items(courseworks) { coursework ->
                CourseworkCard(coursework)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun CourseworkCard(coursework: Coursework) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = coursework.name, style = MaterialTheme.typography.headlineSmall)
            Text(text = coursework.subject, style = MaterialTheme.typography.bodyMedium)
            Text(text = "Due: ${coursework.dueDate}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
