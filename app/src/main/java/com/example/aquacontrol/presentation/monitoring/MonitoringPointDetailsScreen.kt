package com.example.aquacontrol.presentation.monitoring

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aquacontrol.data.model.MonitoringPoint
import com.example.aquacontrol.presentation.components.DropdownSelector
import com.example.aquacontrol.utils.formatDate
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun MonitoringPointDetailsScreen(
    point: MonitoringPoint,
    viewModel: MonitoringViewModel,
    onBack: () -> Unit    // <-- agrega este parámetro
) {
    val state = viewModel.uiState.collectAsState().value

    var active by remember { mutableStateOf(point.active) }

    Column(modifier = Modifier.padding(16.dp)) {
        // Botón para regresar
        Button(onClick = onBack) {
            Text("Regresar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        DropdownSelector(
            label = "Stado",
            options = state.states,
            selectedOption = state.selectedState ?: point.state,
            onOptionSelected = viewModel::onStateSelected
        )
        Spacer(modifier = Modifier.height(8.dp))

        DropdownSelector(
            label = "Municipio",
            options = state.municipalities,
            selectedOption = state.selectedMunicipality ?: point.municipality,
            onOptionSelected = viewModel::onMunicipalitySelected
        )
        Spacer(modifier = Modifier.height(8.dp))

        DropdownSelector(
            label = "Localidad",
            options = state.localities,
            selectedOption = state.selectedLocality ?: point.locality,
            onOptionSelected = viewModel::onLocalitySelected
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text("Latitud: ${state.latitude ?: point.latitude}")
        Text("Longitud: ${state.longitude ?: point.longitude}")

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = active,
                onCheckedChange = { active = it }
            )
            Text("Activo")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val updatedPoint = point.copy(
                state = state.selectedState ?: point.state,
                municipality = state.selectedMunicipality ?: point.municipality,
                locality = state.selectedLocality ?: point.locality,
                latitude = state.latitude ?: point.latitude,
                longitude = state.longitude ?: point.longitude,
                active = active
            )
            viewModel.updateMonitoringPoint(updatedPoint)
        }) {
            Text("Actualizar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { viewModel.deactivateMonitoringPoint(point.id) }) {
            Text("Desactivar")
        }
    }
}

