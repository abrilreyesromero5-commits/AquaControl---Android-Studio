package com.example.aquacontrol.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.aquacontrol.data.model.User
import com.example.aquacontrol.presentation.auth.AuthViewModel
import com.example.aquacontrol.presentation.auth.LoginScreen
import com.example.aquacontrol.presentation.auth.RegisterScreen
import com.example.aquacontrol.presentation.admin.AdminPanel
import com.example.aquacontrol.presentation.general.GeneralPanel
import com.example.aquacontrol.presentation.monitoring.MonitoringPointDetailsScreen
import com.example.aquacontrol.presentation.monitoring.MonitoringViewModel
import com.example.aquacontrol.presentation.visualization.SensorDataScreen


@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val userRole by authViewModel.userRole.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "general"
    ) {
        composable("general") {
            GeneralPanel(
                authViewModel = authViewModel,
                onLoginClick = { navController.navigate("login") },
                navToLogin = {
                    navController.navigate("general") {
                        popUpTo("general") { inclusive = true }
                    }
                }
            )
        }

        composable("login") {
            LoginScreen(
                navController = navController,
                authViewModel = authViewModel,
                onLoginSuccess = {
                    when (authViewModel.userRole.value) {
                        User.UserRole.ADMIN -> navController.navigate("admin") {
                            popUpTo("login") { inclusive = true }
                        }
                        User.UserRole.USER -> navController.navigate("general") {
                            popUpTo("login") { inclusive = true }
                        }
                        else -> Unit
                    }
                }
            )
        }

        composable("register") {
            RegisterScreen(
                navController = navController,
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate("general") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        composable("admin") {
            if (userRole == User.UserRole.ADMIN) {
                AdminPanel(
                    authViewModel = authViewModel,
                    navController = navController,
                    navToLogin = {
                        navController.navigate("login") {
                            popUpTo("admin") { inclusive = true }
                        }
                    }
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate("general") {
                        popUpTo("admin") { inclusive = true }
                    }
                }
            }
        }

        composable("monitoring_details/{pointId}") { backStackEntry ->
            val pointId = backStackEntry.arguments?.getString("pointId") ?: ""
            val viewModel: MonitoringViewModel = hiltViewModel()

            val monitoringPoint by viewModel.observeMonitoringPointById(pointId).collectAsState(initial = null)

            monitoringPoint?.let { point ->
                MonitoringPointDetailsScreen(
                    point = point,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }  // ✅ Ahora pasas el onBack
                )
            }
        }

        composable("sensor_data/{pointId}") { backStackEntry ->
            val pointId = backStackEntry.arguments?.getString("pointId") ?: ""
            SensorDataScreen(
                pointId = pointId,
                onBack = { navController.popBackStack() }
            )
        }


    }
}

