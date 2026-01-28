package com.example.aquacontrol.presentation.visualization

import NeedleGauge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aquacontrol.data.model.SensorReading
import phColor
import temperatureColor
import turbidityColor
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SensorDataScreen(
    pointId: String,
    onBack: () -> Unit,
    viewModel: VisualizeDataViewModel = hiltViewModel()
) {
    val readings by viewModel.sensorReadings.collectAsState()

    val blueRoyalLight = Color(0xFFD0E2FF)
    val blueRoyal = Color(0xFF4169E1)

    LaunchedEffect(pointId) {
        viewModel.selectMonitoringPoint(pointId)
    }

    val latestReading = readings.lastOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(blueRoyalLight)
    ) {
        // Header con botón atrás
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Regresar", tint = blueRoyal)
            }
            Text(
                "Lecturas en tiempo real",
                style = MaterialTheme.typography.titleLarge,
                color = blueRoyal,
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        if (latestReading != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),  // que ocupe solo el espacio necesario
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Top
            ) {
                GaugeWithStatus(
                    label = "pH",
                    value = latestReading.ph,
                    valueRange = 0f..14f,
                    unit = "",
                    colorFunc = ::phColor,
                    getStatus = ::phStatus,
                    size = 150.dp,
                    modifier = Modifier.weight(1f).padding(8.dp)
                )
                GaugeWithStatus(
                    label = "Temperatura",
                    value = latestReading.temperature,
                    valueRange = 0f..50f,
                    unit = "°C",
                    colorFunc = ::temperatureColor,
                    getStatus = ::temperatureStatus,
                    size = 150.dp,
                    modifier = Modifier.weight(1f).padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp)) // menos separación con turbidez

            // Segunda fila: Turbidez centrada y un poco más grande
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentAlignment = Alignment.TopCenter
            ) {
                GaugeWithStatus(
                    label = "Turbidez",
                    value = latestReading.turbidity,
                    valueRange = 0f..100f,
                    unit = "NTU",
                    colorFunc = ::turbidityColor,
                    getStatus = ::turbidityStatus,
                    size = 200.dp,
                    modifier = Modifier.padding(8.dp)
                )
            }
        } else {
            Text(
                "No hay datos para mostrar",
                modifier = Modifier.padding(16.dp),
                color = blueRoyal
            )
        }
    }
}

@Composable
fun GaugeWithStatus(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    unit: String,
    colorFunc: (Float) -> Color,
    getStatus: (Float) -> StatusInfo,
    size: Dp = 150.dp,
    modifier: Modifier = Modifier
) {
    val status = getStatus(value)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        NeedleGauge(
            label = "",  // ocultamos label arriba
            value = value,
            valueRange = valueRange,
            unit = "",
            needleColor = status.color,
            gaugeActiveColor = status.color,
            gaugeBackgroundColor = Color.LightGray,
            modifier = Modifier.size(size)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
            Text(
                text = String.format("%.2f %s", value, unit),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = status.icon,
                contentDescription = status.statusText,
                tint = status.color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = status.statusText,
                style = MaterialTheme.typography.bodyLarge,
                color = status.color,
                textAlign = TextAlign.Center,

            )
        }
    }
}

// Data class y funciones de estado/color/icono

data class StatusInfo(
    val statusText: String,
    val color: Color,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

val softRed = Color(0xFFD32F2F)
val softGreen = Color(0xFF388E3C)
val softBlue = Color(0xFF1976D2)

fun phStatus(ph: Float): StatusInfo = when {
    ph < 6.5f -> StatusInfo("Ácido", softRed, Icons.Filled.Error)
    ph in 6.5f..8.5f -> StatusInfo("Neutro", softGreen, Icons.Filled.CheckCircle)
    else -> StatusInfo("Básico", softRed, Icons.Filled.Error)
}

fun temperatureStatus(temp: Float): StatusInfo = when {
    temp < 15f -> StatusInfo("Fría", softBlue, Icons.Filled.Warning)
    temp in 15f..30f -> StatusInfo("Templada", softGreen, Icons.Filled.CheckCircle)
    else -> StatusInfo("Caliente", softRed, Icons.Filled.Error)
}

fun turbidityStatus(turb: Float): StatusInfo = when {
    turb < 5f -> StatusInfo("Baja", softGreen, Icons.Filled.CheckCircle)
    turb in 5f..50f -> StatusInfo("Media", Color(0xFFFFA500), Icons.Filled.Warning) // naranja
    else -> StatusInfo("Alta", softRed, Icons.Filled.Error)
}