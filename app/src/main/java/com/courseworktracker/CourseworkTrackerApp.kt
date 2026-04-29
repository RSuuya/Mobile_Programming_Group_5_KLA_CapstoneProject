package com.courseworktracker

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.courseworktracker.model.Assignment
import com.courseworktracker.ui.theme.NdejjeCourseworkTrackerTheme
import com.courseworktracker.view.*
import com.courseworktracker.viewmodel.AssignmentViewModel

object Screen {
    const val Login = "login"
    const val Register = "register"
    const val Home = "home"
    const val AddAssignment = "add_assignment"

    const val EditAssignment = "edit_assignment/{assignmentId}"
    const val CoordinatorDashboard = "coordinator_dashboard"
}
fun editAssignmentRoute(id: Int) = "edit_assignment/$id"
@Composable
fun CourseworkTrackerApp() {
    val navController = rememberNavController()
    val viewModel: AssignmentViewModel = hiltViewModel()
    val userPrefs by viewModel.userPreferences.collectAsState()

    // Determine start destination based on login state
    val startDestination = if (userPrefs.isLoggedIn) {
        if (userPrefs.isCoordinator) Screen.CoordinatorDashboard else Screen.Home
    } else {
        Screen.Login
    }

    NdejjeCourseworkTrackerTheme (darkTheme = userPrefs.isDarkMode){
        NavHost(
            navController = navController, 
            startDestination = startDestination
        ) {
            composable(Screen.Login) {
                LoginScreen(
                    onNavigateToRegister = { navController.navigate(Screen.Register) },
                    onLoginSuccess = { isCoordinator, name ->
                        viewModel.updateLoginState(name, isCoordinator, true)
                        val destination = if (isCoordinator) Screen.CoordinatorDashboard else Screen.Home
                        navController.navigate(destination) {
                            popUpTo(Screen.Login) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Register) {
                RegisterScreen(
                    onNavigateToLogin = { navController.navigate(Screen.Login) },
                    onRegisterSuccess = { isCoordinator, name ->
                        viewModel.updateLoginState(name, isCoordinator, true)
                        val destination = if (isCoordinator) Screen.CoordinatorDashboard else Screen.Home
                        navController.navigate(destination) {
                            popUpTo(Screen.Register) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Home) {
                HomeScreen(
                    viewModel = viewModel,
                    userName = userPrefs.userName,
                    isDarkMode = userPrefs.isDarkMode,
                    onToggleDarkMode = { viewModel.toggleDarkMode() },
                    onAddAssignment = { navController.navigate(Screen.AddAssignment) },
                    onEditAssignment = { assignment ->
                        navController.navigate(editAssignmentRoute(assignment.id))
                    },
                    onLogout = {
                        viewModel.logout()
                        navController.navigate(Screen.Login) {
                            popUpTo(Screen.Home) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.AddAssignment) {
                AddAssignmentScreen(
                    onSave = { title, code, lecturer, date, notes ->
                        viewModel.insert(Assignment(
                            title = title, 
                            courseCode = code, 
                            lecturer = lecturer, 
                            dueDate = date,
                            notes = notes,
                            isCompleted = false
                        ))
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.CoordinatorDashboard) {
                CoordinatorDashboardScreen(
                    viewModel = viewModel,
                    userName = userPrefs.userName,
                    onLogout = {
                        viewModel.logout()
                        navController.navigate(Screen.Login) {
                            popUpTo(Screen.CoordinatorDashboard) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.EditAssignment) { backStackEntry ->
                val assignmentId = backStackEntry.arguments?.getString("assignmentId")?.toIntOrNull()
                val assignments by viewModel.allAssignments.collectAsState()
                val assignment = assignments.find { it.id == assignmentId }

                if (assignment != null) {
                    AddAssignmentScreen(
                        existingAssignment = assignment,
                        onSave = { title, code, lecturer, date, notes ->
                            viewModel.update(
                                assignment.copy(
                                    title = title,
                                    courseCode = code,
                                    lecturer = lecturer,
                                    dueDate = date,
                                    notes = notes
                                )
                            )
                            navController.popBackStack()
                        },
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}