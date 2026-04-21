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

@Composable
fun MainApp() {
    val context = LocalContext.current
    val application = context.applicationContext as CourseworkTrackerApplication
    val repository = application.repository
    val viewModelFactory = AssignmentViewModelFactory(repository)
    
    val navController = rememberNavController()
    
    NdejjeCourseworkTrackerTheme {
        NavHost(navController = navController, startDestination = "login") {
            composable("login") {
                LoginScreen(
                    onNavigateToRegister = { navController.navigate("register") },
                    onLoginSuccess = { navController.navigate("home") }
                )
            }
            composable("register") {
                RegisterScreen(
                    onNavigateToLogin = { navController.navigate("login") },
                    onRegisterSuccess = { navController.navigate("home") }
                )
            }
            composable("home") {
                val viewModel: AssignmentViewModel = viewModel(factory = viewModelFactory)
                HomeScreen(
                    viewModel = viewModel,
                    onAddAssignment = { navController.navigate("add_assignment") }
                )
            }
            composable("add_assignment") {
                val viewModel: AssignmentViewModel = viewModel(factory = viewModelFactory)
                AddAssignmentScreen(
                    onSave = { title, code ->
                        viewModel.insert(Assignment(title = title, courseCode = code, dueDate = Date(), isCompleted = false))
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
