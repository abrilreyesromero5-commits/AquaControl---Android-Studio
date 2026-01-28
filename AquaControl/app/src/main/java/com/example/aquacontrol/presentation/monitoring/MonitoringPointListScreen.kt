package com.example.aquacontrol.presentation.monitoring

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aquacontrol.data.model.MonitoringPoint
import com.example.aquacontrol.utils.formatDate

@Composable
fun MonitoringPointListScreen(
    viewModel: MonitoringViewModel = hiltViewModel(),
    onItemClick: (MonitoringPoint) -> Unit
) {
    val points = viewModel.monitoringPoints

    LaunchedEffect(Unit) {
        viewModel.loadMonitoringPoints()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Puntos de Monitoreo", style = MaterialTheme.typography.titleLarge)

        LazyColumn {
            items(points) { point ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onItemClick(point) }
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("${point.state}, ${point.municipality}, ${point.locality}")
                        Text("Lat: ${point.latitude}, Lon: ${point.longitude}")
                        Text("Activo: ${if (point.isActive) "Sí" else "No"}")
                        Text("Fecha: ${formatDate(point.createdAt)}")
                    }
                }
            }
        }
    }
}

