package com.courseworktracker

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.courseworktracker.model.Assignment
import com.courseworktracker.ui.theme.NdejjeCourseworkTrackerTheme
import com.courseworktracker.view.*
import com.courseworktracker.viewmodel.AssignmentViewModel
import java.util.Date

private val hilt: Any
    get() {
        val todo = TODO()
    }

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
    val viewModel: AssignmentViewModel = hiltViewModel
    val userPrefs by viewModel.userPreferences.collectAsState()

    val startDestination = if (userPrefs.isLoggedIn) {
        if (userPrefs.isCoordinator) Screen.CoordinatorDashboard else Screen.Home
    } else {
        Screen.Login
    }

    NdejjeCourseworkTrackerTheme(darkTheme = userPrefs.isDarkMode) {
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
                val function = { title, code, lecturer, date ->
                    viewModel.insert(
                        Assignment(
                            title = title,
                            courseCode = code,
                            lecturer = lecturer,
                            dueDate = date,
                            isCompleted = false
                        )
                    )
                    navController.popBackStack()
                } as (String, String, String, Date, String) -> Unit,
                        TODO(),
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

            Box {
                composable(
                    Screen.EditAssignment,
                    listOf(navArgument("assignmentId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val assignmentId = backStackEntry.arguments?.getInt("assignmentId")
                    val assignments by viewModel.allAssignments.collectAsState(initial = emptyList())
                    val assignment = assignments.find { it.id == assignmentId }

                    assignment?.let { existingAssignment ->
                        AddAssignmentScreen(
                            existingAssignment = existingAssignment,
                            onSave = { title, code, lecturer, date ->
                                viewModel.update(
                                    existingAssignment.copy(
                                        title = title,
                                        courseCode = code,
                                        lecturer = lecturer,
                                        dueDate = date
                                    )
                                )
                                navController.popBackStack()
                            } as (String, String, String, Date, String) -> Unit,
                            onBack = { navController.popBackStack() },
                            viewModel = TODO(),
                            modifier = TODO()
                        )
                    }
                }
            }
        }
    }
}

private fun NavGraphBuilder.composable(
    route: String,
    arguments: List<NamedNavArgument>,
    content: Any
) {
}

private fun NavGraphBuilder.composable(route: String, content: () -> Unit) {}
