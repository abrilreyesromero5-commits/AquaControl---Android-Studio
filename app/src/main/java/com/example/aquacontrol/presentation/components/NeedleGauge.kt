import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@Composable
fun NeedleGauge(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    unit: String,
    modifier: Modifier = Modifier,
    needleColor: Color,
    gaugeBackgroundColor: Color = Color.LightGray,
    gaugeActiveColor: Color,
    maxAngle: Float = 270f
) {
    val normalizedValue = ((value - valueRange.start) / (valueRange.endInclusive - valueRange.start))
        .coerceIn(0f, 1f)

    val startAngle = 135f
    val sweepAngle = maxAngle
    val needleAngle = startAngle + sweepAngle * normalizedValue
    val activeSweep = sweepAngle * normalizedValue

    Box(modifier = modifier, contentAlignment = androidx.compose.ui.Alignment.Center) {
        Canvas(modifier = Modifier.size(150.dp)) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val radius = min(canvasWidth, canvasHeight) / 2 * 0.8f
            val center = Offset(canvasWidth / 2, canvasHeight / 2)

            // Fondo
            drawArc(
                color = gaugeBackgroundColor,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = 20f)
            )

            // Arco activo
            drawArc(
                color = gaugeActiveColor,
                startAngle = startAngle,
                sweepAngle = activeSweep,
                useCenter = false,
                style = Stroke(width = 20f)
            )

            // Aguja
            val angleRad = Math.toRadians(needleAngle.toDouble())
            val needleLength = radius * 0.9f
            val needleEnd = Offset(
                x = center.x + needleLength * cos(angleRad).toFloat(),
                y = center.y + needleLength * sin(angleRad).toFloat()
            )

            drawLine(
                color = needleColor,
                start = center,
                end = needleEnd,
                strokeWidth = 6f
            )

            // Centro aguja
            drawCircle(color = needleColor, radius = 12f, center = center)
        }

        Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
            Text(text = label, style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)
            Text(text = String.format("%.2f %s", value, unit), style = androidx.compose.material3.MaterialTheme.typography.bodySmall)
        }
    }
}

// Funciones para determinar color según valor

fun phColor(ph: Float): Color = when {
    ph < 6.5f -> Color.Red           // Ácido, peligro
    ph in 6.5f..8.5f -> Color.Green // Normal
    else -> Color.Red               // Alcalino, peligro
}

fun temperatureColor(temp: Float): Color = when {
    temp < 0f -> Color.Blue         // Bajo cero, frío extremo
    temp in 0f..35f -> Color.Green  // Normal rango apto para agua potable
    temp in 35f..45f -> Color.Yellow// Precaución
    else -> Color.Red               // Caliente, peligro
}

fun turbidityColor(turb: Float): Color = when {
    turb < 5f -> Color.Green        // Agua clara
    turb in 5f..50f -> Color.Yellow // Moderada turbidez
    else -> Color.Red               // Alta turbidez, peligro
}

fun getPhStatus(ph: Float): String = when {
    ph < 6.5f -> "Ácido"
    ph <= 8.5f -> "Neutro"
    else -> "Básico"
}

fun getTemperatureStatus(temp: Float): String = when {
    temp < 15f -> "Fría"
    temp <= 30f -> "Templada"
    else -> "Caliente"
}

fun getTurbidityStatus(turb: Float): String = when {
    turb < 5f -> "Baja"
    turb <= 50f -> "Media"
    else -> "Alta"
}
