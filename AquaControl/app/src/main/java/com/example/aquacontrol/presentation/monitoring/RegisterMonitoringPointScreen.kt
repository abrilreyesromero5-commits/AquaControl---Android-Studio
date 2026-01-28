package com.example.aquacontrol.presentation.monitoring

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aquacontrol.data.model.MonitoringPoint
import com.example.aquacontrol.presentation.components.DropdownSelector
import com.example.aquacontrol.utils.loadJsonFromAssets


@Composable
fun RegisterMonitoringPointScreen(
    viewModel: MonitoringViewModel = hiltViewModel(),
    onSaveSuccess: () -> Unit
) {
    val context = LocalContext.current
    val locationData = remember { loadJsonFromAssets(context, "republica.json") }

    var selectedState by remember { mutableStateOf("") }
    var selectedMunicipality by remember { mutableStateOf("") }
    var selectedLocality by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(true) }

    var latitude by remember { mutableStateOf(0.0) }
    var longitude by remember { mutableStateOf(0.0) }

    // Listados para dropdowns
    val estados = locationData.keys.toList()
    val municipalities = locationData[selectedState]?.keys?.toList() ?: emptyList()
    val localities = locationData[selectedState]?.get(selectedMunicipality) ?: emptyList()
    val localitiesNames = localities.map { it.nombre }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Registrar Punto de Monitoreo", style = MaterialTheme.typography.titleLarge)

        DropdownSelector(
            label = "Estado",
            options = estados,
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

        Button(
            onClick = {
                if (selectedState.isNotEmpty() && selectedMunicipality.isNotEmpty() && selectedLocality.isNotEmpty()) {
                    val point = MonitoringPoint(
                        state = selectedState,
                        municipality = selectedMunicipality,
                        locality = selectedLocality,
                        latitude = latitude,
                        longitude = longitude,
                        isActive = isActive
                    )
                    viewModel.registerMonitoringPoint(point)
                    onSaveSuccess()
                }
            }
        ) {
            Text("Guardar")
        }
    }
}
