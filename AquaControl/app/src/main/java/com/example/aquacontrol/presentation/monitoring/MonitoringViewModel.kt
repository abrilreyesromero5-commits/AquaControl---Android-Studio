package com.example.aquacontrol.presentation.monitoring

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aquacontrol.data.model.MonitoringPoint
import com.example.aquacontrol.data.model.SensorReading
import com.example.aquacontrol.domain.usecase.MonitoringUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MonitoringViewModel @Inject constructor(
    private val useCases: MonitoringUseCases
) : ViewModel() {

    val monitoringPoints = mutableStateListOf<MonitoringPoint>()

    // Variables para latitud y longitud seleccionadas
    var latitude = mutableStateOf(0.0)
    var longitude = mutableStateOf(0.0)

    // Métodos para actualizar lat y lon, por ejemplo:
    fun setLatitude(lat: Double) { latitude.value = lat }
    fun setLongitude(lon: Double) { longitude.value = lon }

    fun registerMonitoringPoint(point: MonitoringPoint) = viewModelScope.launch {
        useCases.register(point).onSuccess {
            monitoringPoints.add(point)
        }
    }

    fun loadMonitoringPoints() = viewModelScope.launch {
        useCases.getAll().onSuccess {
            monitoringPoints.clear()
            monitoringPoints.addAll(it)
        }
    }

    fun updateMonitoringPoint(point: MonitoringPoint) = viewModelScope.launch {
        useCases.update(point)
    }

    fun deleteMonitoringPoint(id: String) = viewModelScope.launch {
        useCases.delete(id)
    }
}

