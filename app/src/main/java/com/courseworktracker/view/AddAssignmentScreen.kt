package com.courseworktracker.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.courseworktracker.R
import com.courseworktracker.ui.theme.NdejjeCourseworkTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAssignmentScreen(
    onSave: (String, String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }
    var courseCode by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.add_coursework)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_button)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(stringResource(id = R.string.assignment_title)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = courseCode,
                onValueChange = { courseCode = it },
                label = { Text(stringResource(id = R.string.course_code)) },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { 
                    if (title.isNotBlank() && courseCode.isNotBlank()) {
                        onSave(title, courseCode)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(id = R.string.save_button))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddAssignmentPreview() {
    NdejjeCourseworkTrackerTheme {
        AddAssignmentScreen(onSave = { _, _ -> }, onBack = {})
    }
}
