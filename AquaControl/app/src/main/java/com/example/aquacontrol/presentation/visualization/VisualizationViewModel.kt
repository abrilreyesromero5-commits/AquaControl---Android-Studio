package com.example.aquacontrol.presentation.visualization

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aquacontrol.data.model.MonitoringPoint
import com.example.aquacontrol.data.model.SensorReading
import com.example.aquacontrol.domain.usecase.MonitoringUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class VisualizationViewModel @Inject constructor(
    private val useCases: MonitoringUseCases
) : ViewModel() {

    // Flow con los puntos activos
    val monitoringPoints = useCases.getMonitoringPoints()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Estado para las lecturas del punto seleccionado
    private val _sensorReadings = MutableStateFlow<List<SensorReading>>(emptyList())
    val sensorReadings: StateFlow<List<SensorReading>> = _sensorReadings

    // Función para comenzar a observar lecturas de un punto
    fun observeSensorReadings(pointId: String) {
        viewModelScope.launch {
            useCases.getSensorReadings(pointId).collect {
                _sensorReadings.value = it
            }
        }
    }
}