package com.example.aquacontrol.presentation.report

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aquacontrol.presentation.components.DropdownSelector
import com.example.aquacontrol.utils.JsonUtils
import java.util.Calendar

@Composable
fun ReportScreen(viewModel: ReportViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState
    val context = LocalContext.current
    val customButtonColor = Color(0xFF0A74DA)

    val estados by produceState(initialValue = emptyList<String>()) {
        val republic = JsonUtils.loadRepublicFromAssets(context)
        value = republic?.states?.map { it.nombre } ?: emptyList()
    }

    var selectedState by remember { mutableStateOf<String?>(null) }
    var selectedMonth by remember { mutableStateOf<String?>(null) }
    var selectedYear by remember { mutableStateOf<String?>(null) }

    val horizontalScrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.loadReports(state = null, month = null, year = null)
    }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            DropdownSelector("Estado", estados, selectedState) { selectedState = it }
            DropdownSelector("Mes", (1..12).map { it.toString() }, selectedMonth) { selectedMonth = it }
            DropdownSelector(
                "Año",
                (2022..Calendar.getInstance().get(Calendar.YEAR)).map { it.toString() },
                selectedYear
            ) { selectedYear = it }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        viewModel.loadReports(
                            state = selectedState,
                            month = selectedMonth?.toIntOrNull(),
                            year = selectedYear?.toIntOrNull()
                        )
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = customButtonColor)

                ) {
                    Text("Buscar", color = Color.White)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        val csv = viewModel.generateCsvReport(uiState.reportEntries)
                        viewModel.saveCsvToFile(context, csv, "reporte.csv")
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = customButtonColor)

                ) {
                    Text("CSV", color = Color.White)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        viewModel.generatePdfReport(context, uiState.reportEntries, "reporte.pdf")
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = customButtonColor)
                ) {
                    Text("PDF", color = Color.White)
                }
            }

            if (uiState.isLoading) {
                CircularProgressIndicator()
            }
        }

        if (!uiState.isLoading && uiState.reportEntries.isNotEmpty()) {
            item {
                Box(modifier = Modifier.horizontalScroll(horizontalScrollState)) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .width(900.dp)
                        ) {
                            Text("Estado", Modifier.weight(1f).padding(4.dp))
                            Text("Municipio", Modifier.weight(1f).padding(4.dp))
                            Text("Localidad", Modifier.weight(1f).padding(4.dp))
                            Text("pH", Modifier.weight(0.7f).padding(4.dp))
                            Text("Temp", Modifier.weight(0.7f).padding(4.dp))
                            Text("Turb", Modifier.weight(0.7f).padding(4.dp))
                            Text("Día", Modifier.weight(0.7f).padding(4.dp))
                            Text("Mes", Modifier.weight(0.7f).padding(4.dp))
                            Text("Año", Modifier.weight(0.7f).padding(4.dp))
                        }
                        Divider()
                    }
                }
            }

            items(uiState.reportEntries) { entry ->
                Box(modifier = Modifier.horizontalScroll(horizontalScrollState)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                            .width(900.dp)
                    ) {
                        val calendar = Calendar.getInstance().apply { timeInMillis = entry.reading.timestamp }
                        Text(entry.state, Modifier.weight(1f).padding(4.dp))
                        Text(entry.municipality, Modifier.weight(1f).padding(4.dp))
                        Text(entry.locality, Modifier.weight(1f).padding(4.dp))
                        Text(entry.reading.ph.toString(), Modifier.weight(0.7f).padding(4.dp))
                        Text(entry.reading.temperature.toString(), Modifier.weight(0.7f).padding(4.dp))
                        Text(entry.reading.turbidity.toString(), Modifier.weight(0.7f).padding(4.dp))
                        Text(calendar.get(Calendar.DAY_OF_MONTH).toString(), Modifier.weight(0.7f).padding(4.dp))
                        Text((calendar.get(Calendar.MONTH) + 1).toString(), Modifier.weight(0.7f).padding(4.dp))
                        Text(calendar.get(Calendar.YEAR).toString(), Modifier.weight(0.7f).padding(4.dp))
                    }
                    Divider()
                }
            }
        }

        if (!uiState.isLoading && uiState.reportEntries.isEmpty()) {
            item {
                Text("No se encontraron registros.", Modifier.padding(8.dp))
            }
        }
    }
}
