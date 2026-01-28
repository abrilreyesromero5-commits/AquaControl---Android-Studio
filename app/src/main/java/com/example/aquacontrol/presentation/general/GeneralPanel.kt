package com.example.aquacontrol.presentation.general

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aquacontrol.data.model.User
import com.example.aquacontrol.presentation.auth.AuthViewModel
import com.example.aquacontrol.presentation.components.NavigationDrawer
import com.example.aquacontrol.presentation.dashboard.DashboardScreen
import com.example.aquacontrol.presentation.report.ReportScreen
import com.example.aquacontrol.presentation.visualization.SensorDataScreen
import com.example.aquacontrol.presentation.visualization.VisualizeDataScreen

import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralPanel(
    authViewModel: AuthViewModel,
    onLoginClick: () -> Unit,
    navToLogin: () -> Unit
) {
    var selectedOption by remember { mutableStateOf("visualizar_datos") }
    var selectedSensorPointId by remember { mutableStateOf<String?>(null) }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawer(
                role = User.UserRole.USER,
                selectedItem = selectedOption,
                onItemClick = { option ->
                    selectedOption = option
                    selectedSensorPointId = null // Limpiar selección si cambio de opción
                    scope.launch { drawerState.close() }
                },
                onLogout = {
                    authViewModel.logout {
                        navToLogin()
                    }
                }
            )
        },
        scrimColor = Color.Black.copy(alpha = 0.4f)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("AquaControl") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    },
                    actions = {
                        TextButton(onClick = onLoginClick) {
                            Text("Iniciar Sesión")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                when (selectedOption) {
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
                    "reportes" -> ReportScreen()
                    "visualizar_predicciones" -> DashboardScreen()
                    else -> Text("Selecciona una opción del menú")
                }
            }
        }
    }
}


