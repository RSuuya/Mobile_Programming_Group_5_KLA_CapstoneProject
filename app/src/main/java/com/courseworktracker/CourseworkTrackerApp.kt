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
    const val CoordinatorUpload = "coordinator_upload"
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
                    onLoginSuccess = { 
                        navController.navigate(Screen.Home) {
                            popUpTo(Screen.Login) { inclusive = true }
                        }
                    },
                    onNavigateToCoordinator = { navController.navigate(Screen.CoordinatorUpload) }
                )
            }
            composable(Screen.Register) {
                RegisterScreen(
                    onNavigateToLogin = { navController.navigate(Screen.Login) },
                    onRegisterSuccess = { 
                        navController.navigate(Screen.Home) {
                            popUpTo(Screen.Register) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Home) {
                val viewModel: AssignmentViewModel = viewModel(factory = viewModelFactory)
                HomeScreen(
                    viewModel = viewModel,
                    onAddAssignment = { navController.navigate(Screen.AddAssignment) }
                )
            }
            composable(Screen.AddAssignment) {
                val viewModel: AssignmentViewModel = viewModel(factory = viewModelFactory)
                AddAssignmentScreen(
                    onSave = { title, code, date ->
                        viewModel.insert(Assignment(title = title, courseCode = code, dueDate = date, isCompleted = false))
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.CoordinatorUpload) {
                val viewModel: AssignmentViewModel = viewModel(factory = viewModelFactory)
                CoordinatorUploadScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
