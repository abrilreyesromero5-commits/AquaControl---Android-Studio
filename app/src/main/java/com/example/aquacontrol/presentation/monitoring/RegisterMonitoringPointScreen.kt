package com.example.aquacontrol.presentation.monitoring

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.aquacontrol.data.model.MonitoringPoint
import com.example.aquacontrol.presentation.components.DropdownSelector
import com.example.aquacontrol.utils.JsonUtils
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun RegisterMonitoringPointScreen(
    viewModel: MonitoringViewModel = viewModel()
) {
    val state = viewModel.uiState.collectAsState().value

    // Estado local para switch (activo/inactivo)
    var active by remember { mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(state.successMessage) {
        state.successMessage?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
                viewModel.clearSuccessMessage()
            }
        }
    }


    Column(modifier = Modifier.padding(16.dp)) {

        DropdownSelector(
            label = "Estado",
            options = state.states,
            selectedOption = state.selectedState,
            onOptionSelected = viewModel::onStateSelected
        )
        SnackbarHost(hostState = snackbarHostState)

        DropdownSelector(
            label = "Municipio",
            options = state.municipalities,
            selectedOption = state.selectedMunicipality,
            onOptionSelected = viewModel::onMunicipalitySelected
        )
        Spacer(modifier = Modifier.height(8.dp))

        DropdownSelector(
            label = "Localidad",
            options = state.localities,
            selectedOption = state.selectedLocality,
            onOptionSelected = viewModel::onLocalitySelected
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text("Latitud: ${state.latitude ?: ""}")
        Text("Longitud: ${state.longitude ?: ""}")

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = if (active) "Activo" else "Inactivo")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = active,
                onCheckedChange = { active = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.registerMonitoringPoint(active)
                // Limpiar selecciones y switch después de registrar
                active = true
                viewModel.clearSelections()
            },
            enabled = !state.isLoading
        ) {
            Text("Registrar")
        }

        state.errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
    }
}


