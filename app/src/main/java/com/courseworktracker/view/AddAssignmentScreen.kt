package com.courseworktracker.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.courseworktracker.R
import com.courseworktracker.model.Assignment
import com.courseworktracker.ui.theme.NdejjeCourseworkTrackerTheme
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAssignmentScreen(
    onSave: (String, String, String, Date) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    existingAssignment: Assignment? = null
) {
    var title by remember { mutableStateOf(existingAssignment?.title ?: "") }
    var selectedCourse by remember { mutableStateOf(existingAssignment?.courseCode ?: "") }
    var lecturer by remember { mutableStateOf(existingAssignment?.lecturer ?: "") }
    var selectedDate by remember { mutableLongStateOf(existingAssignment?.dueDate?.time ?: System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    
    var titleError by remember { mutableStateOf(false) }
    var courseError by remember { mutableStateOf(false) }

    val courses = listOf(
        "BCS2201 - Mobile App Dev",
        "BIT2205 - Database Systems",
        "BSE3102 - Network Security",
        "BCS2104 - Data Structures",
        "BIT1203 - Programming I",
        "BSE4101 - Software Eng"
    )

    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDate)
    val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedDate = datePickerState.selectedDateMillis ?: selectedDate
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text( text = if (existingAssignment != null) "Edit Coursework"
                        else stringResource(id = R.string.add_coursework)) },
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
            // Assignment Title
            OutlinedTextField(
                value = title,
                onValueChange = { 
                    title = it
                    titleError = false
                },
                label = { Text(stringResource(id = R.string.assignment_title)) },
                isError = titleError,
                supportingText = {
                    if (titleError) Text("Title is required", color = MaterialTheme.colorScheme.error)
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Course Unit Dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedCourse,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(id = R.string.course_code)) },
                    isError = courseError,
                    supportingText = {
                        if (courseError) Text("Please select a course", color = MaterialTheme.colorScheme.error)
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    courses.forEach { course ->
                        DropdownMenuItem(
                            text = { Text(course) },
                            onClick = {
                                selectedCourse = course
                                courseError = false
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Lecturer Name
            OutlinedTextField(
                value = lecturer,
                onValueChange = { lecturer = it },
                label = { Text("Lecturer Name") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            // Date Picker Field
            OutlinedTextField(
                value = dateFormatter.format(Date(selectedDate)),
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(id = R.string.due_date)) },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    titleError = title.isBlank()
                    courseError = selectedCourse.isBlank()
                    
                    if (!titleError && !courseError) {
                        onSave(title, selectedCourse.split(" - ")[0], lecturer, Date(selectedDate))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    if (existingAssignment != null) "Update"
                        else stringResource(id = R.string.save_button))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddAssignmentPreview() {
    NdejjeCourseworkTrackerTheme {
        AddAssignmentScreen(onSave = { _, _, _, _ -> }, onBack = {})
    }
}
