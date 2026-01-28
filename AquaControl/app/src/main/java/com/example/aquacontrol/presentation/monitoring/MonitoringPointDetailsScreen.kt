package com.example.aquacontrol.presentation.monitoring

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.aquacontrol.utils.loadJsonFromAssets


@Composable
fun MonitoringPointDetailsScreen(
    point: MonitoringPoint,
    viewModel: MonitoringViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val locationData = remember { loadJsonFromAssets(context, "republica.json") }

    var selectedState by remember { mutableStateOf(point.state) }
    var selectedMunicipality by remember { mutableStateOf(point.municipality) }
    var selectedLocality by remember { mutableStateOf(point.locality) }
    var isActive by remember { mutableStateOf(point.isActive) }

    var latitude by remember { mutableStateOf(point.latitude) }
    var longitude by remember { mutableStateOf(point.longitude) }

    val municipalities = locationData[selectedState]?.keys?.toList() ?: emptyList()
    val localities = locationData[selectedState]?.get(selectedMunicipality) ?: emptyList()
    val localitiesNames = localities.map { it.nombre }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Detalles del Punto", style = MaterialTheme.typography.titleLarge)

        DropdownSelector(
            label = "Estado",
            options = locationData.keys.toList(),
            selectedOption = selectedState,
            onOptionSelected = {
                selectedState = it
                selectedMunicipality = ""
                selectedLocality = ""
                latitude = 0.0
                longitude = 0.0
            }
        )

        if (selectedState.isNotEmpty()) {
            DropdownSelector(
                label = "Municipio",
                options = municipalities,
                selectedOption = selectedMunicipality,
                onOptionSelected = {
                    selectedMunicipality = it
                    selectedLocality = ""
                    latitude = 0.0
                    longitude = 0.0
                }
            )
        }

        if (selectedMunicipality.isNotEmpty()) {
            DropdownSelector(
                label = "Localidad",
                options = localitiesNames,
                selectedOption = selectedLocality,
                onOptionSelected = { locName ->
                    selectedLocality = locName
                    val locObj = localities.find { it.nombre == locName }
                    latitude = locObj?.latitud ?: 0.0
                    longitude = locObj?.longitud ?: 0.0
                }
            )
        }

        Spacer(Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Activo")
            Switch(checked = isActive, onCheckedChange = { isActive = it })
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            if (selectedState.isNotEmpty() && selectedMunicipality.isNotEmpty() && selectedLocality.isNotEmpty()) {
                val updatedPoint = point.copy(
                    state = selectedState,
                    municipality = selectedMunicipality,
                    locality = selectedLocality,
                    latitude = latitude,
                    longitude = longitude,
                    isActive = isActive
                )
                viewModel.updateMonitoringPoint(updatedPoint)
                onBack()
            }
        }) {
            Text("Guardar Cambios")
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.deleteMonitoringPoint(point.id)
                onBack()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Eliminar Punto", color = Color.White)
        }
    }
}
