package com.example.aquacontrol.presentation.admin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.aquacontrol.data.model.MonitoringPoint
import com.example.aquacontrol.data.model.User
import com.example.aquacontrol.presentation.auth.AuthViewModel
import com.example.aquacontrol.presentation.components.NavigationDrawer
import com.example.aquacontrol.presentation.dashboard.DashboardScreen
import com.example.aquacontrol.presentation.monitoring.MonitoringPointDetailsScreen
import com.example.aquacontrol.presentation.monitoring.MonitoringPointListScreen
import com.example.aquacontrol.presentation.monitoring.MonitoringViewModel
import com.example.aquacontrol.presentation.monitoring.RegisterMonitoringPointScreen
import com.example.aquacontrol.presentation.report.ReportScreen
import com.example.aquacontrol.presentation.visualization.SensorDataScreen
import com.example.aquacontrol.presentation.visualization.VisualizeDataScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanel(
    authViewModel: AuthViewModel,
    navController: NavHostController,
    navToLogin: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var selectedOption by remember { mutableStateOf("gestion_puntos") }
    var selectedMonitoringPoint by remember { mutableStateOf<MonitoringPoint?>(null) }
    var selectedSensorPointId by remember { mutableStateOf<String?>(null) } // Para SensorDataScreen

    val monitoringViewModel: MonitoringViewModel = hiltViewModel()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawer(
                role = User.UserRole.ADMIN,
                selectedItem = selectedOption,
                onItemClick = { option ->
                    selectedOption = option
                    selectedMonitoringPoint = null  // Limpiar punto seleccionado al cambiar menú
                    selectedSensorPointId = null    // Limpiar punto de sensor seleccionado también
                    scope.launch { drawerState.close() }
                },
                onLogout = {
                    authViewModel.logout {
                        navToLogin()
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("AquaControl") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menú")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                if (selectedMonitoringPoint != null) {
                    MonitoringPointDetailsScreen(
                        point = selectedMonitoringPoint!!,
                        viewModel = monitoringViewModel,
                        onBack = { selectedMonitoringPoint = null }
                    )
                } else {
                    when (selectedOption) {
                        "registrar_punto" -> RegisterMonitoringPointScreen()
                        "gestion_puntos" -> MonitoringPointListScreen(
                            onPointSelected = { point ->
                                selectedMonitoringPoint = point
                            }
                        )
                        "visualizar_datos" -> {
                            if (selectedSensorPointId != null) {
                                SensorDataScreen(
                                    pointId = selectedSensorPointId!!,
                                    onBack = { selectedSensorPointId = null }
                                )
                            } else {
                                VisualizeDataScreen(
                                    onPointSelected = { point ->
                                        selectedSensorPointId = point.id
                                    }
                                )
                            }
                        }
                        "historial_alertas" -> Text("Historial Alertas")
                        "reportes" -> ReportScreen()
                        "visualizar_predicciones" -> DashboardScreen()
                    }
                }
            }
        }
    }
}


