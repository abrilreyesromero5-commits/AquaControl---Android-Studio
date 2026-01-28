package com.example.aquacontrol.presentation.visualization

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aquacontrol.presentation.components.MapComponent
import com.example.aquacontrol.data.model.MonitoringPoint



@Composable
fun VisualizeDataScreen(
    viewModel: VisualizeDataViewModel = hiltViewModel(),
    onPointSelected: (MonitoringPoint) -> Unit
) {
    val points by viewModel.monitoringPoints.collectAsState()

    Column {
        Text("Seleccione un punto de monitoreo")
        MapComponent(
            points = points,
            onPointSelected = { point ->
                onPointSelected(point)
            }
        )
    }
}

