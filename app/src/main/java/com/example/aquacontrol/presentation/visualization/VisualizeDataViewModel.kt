package com.example.aquacontrol.presentation.visualization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aquacontrol.data.model.MonitoringPoint
import com.example.aquacontrol.data.model.SensorReading
import com.example.aquacontrol.data.repository.MonitoringPointRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class VisualizeDataViewModel @Inject constructor(
    private val repository: MonitoringPointRepository
) : ViewModel() {

    val monitoringPoints: StateFlow<List<MonitoringPoint>> = repository.getAllMonitoringPoints()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _selectedPointId = MutableStateFlow<String?>(null)

    // Lecturas crudas en tiempo real para el punto seleccionado
    val sensorReadings: StateFlow<List<SensorReading>> = _selectedPointId
        .filterNotNull()
        .flatMapLatest { pointId ->
            repository.getSensorReadingsByPointId(pointId)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Función para seleccionar punto
    fun selectMonitoringPoint(pointId: String) {
        _selectedPointId.value = pointId
    }
}