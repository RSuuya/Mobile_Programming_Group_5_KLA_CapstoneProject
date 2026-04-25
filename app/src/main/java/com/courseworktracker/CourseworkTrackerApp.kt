package com.courseworktracker

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.courseworktracker.model.Assignment
import com.courseworktracker.ui.theme.NdejjeCourseworkTrackerTheme
import com.courseworktracker.view.*
import com.courseworktracker.viewmodel.AssignmentViewModel
import com.courseworktracker.viewmodel.AssignmentViewModelFactory
import java.util.Date

object Screen {
    const val Login = "login"
    const val Register = "register"
    const val Home = "home"
    const val AddAssignment = "add_assignment"
    const val CoordinatorDashboard = "coordinator_dashboard"
}

@Composable
fun CourseworkTrackerApp() {
    val context = LocalContext.current
    val application = context.applicationContext as CourseworkTrackerApplication
    val repository = application.repository
    val viewModelFactory = AssignmentViewModelFactory(repository)
    
    val navController = rememberNavController()
    
    NdejjeCourseworkTrackerTheme {
        NavHost(navController = navController, startDestination = Screen.Login) {
            composable(Screen.Login) {
                LoginScreen(
                    onNavigateToRegister = { navController.navigate(Screen.Register) },
                    onLoginSuccess = { isCoordinator ->
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
                    onRegisterSuccess = { isCoordinator ->
                        val destination = if (isCoordinator) Screen.CoordinatorDashboard else Screen.Home
                        navController.navigate(destination) {
                            popUpTo(Screen.Register) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Home) {
                val viewModel: AssignmentViewModel = viewModel(factory = viewModelFactory)
                HomeScreen(
                    viewModel = viewModel,
                    onAddAssignment = { navController.navigate(Screen.AddAssignment) },
                    onLogout = {
                        navController.navigate(Screen.Login) {
                            popUpTo(Screen.Home) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.AddAssignment) {
                val viewModel: AssignmentViewModel = viewModel(factory = viewModelFactory)
                AddAssignmentScreen(
                    onSave = { title, code, lecturer, date ->
                        viewModel.insert(Assignment(
                            title = title, 
                            courseCode = code, 
                            lecturer = lecturer, 
                            dueDate = date, 
                            isCompleted = false
                        ))
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.CoordinatorDashboard) {
                val viewModel: AssignmentViewModel = viewModel(factory = viewModelFactory)
                CoordinatorDashboardScreen(
                    viewModel = viewModel,
                    onLogout = {
                        navController.navigate(Screen.Login) {
                            popUpTo(Screen.CoordinatorDashboard) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
