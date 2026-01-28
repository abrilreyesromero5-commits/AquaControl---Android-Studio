package com.example.aquacontrol.presentation.report

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aquacontrol.data.model.ReportEntry
import com.example.aquacontrol.domain.usecase.GetReportEntriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val useCase: GetReportEntriesUseCase.GetReports
) : ViewModel() {

    var uiState by mutableStateOf(ReportUiState())
        private set

    fun loadReports(state: String?, month: Int?, year: Int?) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            try {
                val reports = useCase(state, month, year)
                uiState = uiState.copy(isLoading = false, reportEntries = reports)
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun generateCsvReport(entries: List<ReportEntry>): String {
        val header = "Estado,Municipio,Localidad,pH,Temperatura,Turbidez,Día,Mes,Año"
        val rows = entries.map { entry ->
            val calendar = Calendar.getInstance().apply { timeInMillis = entry.reading.timestamp }
            listOf(
                entry.state,
                entry.municipality,
                entry.locality,
                entry.reading.ph.toString(),
                entry.reading.temperature.toString(),
                entry.reading.turbidity.toString(),
                calendar.get(Calendar.DAY_OF_MONTH).toString(),
                (calendar.get(Calendar.MONTH) + 1).toString(),
                calendar.get(Calendar.YEAR).toString()
            ).joinToString(",")
        }
        return (listOf(header) + rows).joinToString("\n")
    }

    fun generatePdfReport(context: Context, entries: List<ReportEntry>, fileName: String) {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()
        paint.textSize = 12f

        var y = 30f
        canvas.drawText("Reporte de Calidad del Agua", 50f, y, paint)
        y += 30f

        entries.forEach { entry ->
            val calendar = Calendar.getInstance().apply { timeInMillis = entry.reading.timestamp }
            val line = "${entry.state}, ${entry.municipality}, ${entry.locality}, pH: ${entry.reading.ph}, " +
                    "Temp: ${entry.reading.temperature}, Turb: ${entry.reading.turbidity}, " +
                    "Fecha: ${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"
            canvas.drawText(line, 50f, y, paint)
            y += 20f
        }

        document.finishPage(page)

        try {
            val file = File(context.getExternalFilesDir(null), fileName)
            document.writeTo(FileOutputStream(file))
            Toast.makeText(context, "PDF guardado en ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error al guardar PDF", Toast.LENGTH_LONG).show()
        }

        document.close()
    }

    fun saveCsvToFile(context: Context, csvData: String, fileName: String) {
        try {
            val file = File(context.getExternalFilesDir(null), fileName)
            FileOutputStream(file).use { output ->
                output.write(csvData.toByteArray())
            }
            Toast.makeText(context, "CSV guardado en ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error al guardar CSV", Toast.LENGTH_LONG).show()
        }
    }


}
