package com.example.aquacontrol.presentation.monitoring

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aquacontrol.data.model.MonitoringPoint
import com.example.aquacontrol.utils.formatDate
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MonitoringPointListScreen(
    onPointSelected: (MonitoringPoint) -> Unit,
    viewModel: MonitoringViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState().value

    LazyColumn {
        items(state.monitoringPoints) { point ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onPointSelected(point) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Estado: ${point.state}")
                    Text("Municipio: ${point.municipality}")
                    Text("Localidad: ${point.locality}")
                }
            }
        }
    }
}

