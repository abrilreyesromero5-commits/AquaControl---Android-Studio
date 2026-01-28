package com.example.aquacontrol.domain.usecase

import com.example.aquacontrol.data.model.MonitoringPoint
import com.example.aquacontrol.data.model.SensorReading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow

data class MonitoringUseCases(
    val register: suspend (MonitoringPoint) -> Result<Unit>,
    val getAll: suspend () -> Result<List<MonitoringPoint>>,
    val update: suspend (MonitoringPoint) -> Result<Unit>,
    val delete: suspend (String) -> Result<Unit>,
    val getMonitoringPoints: () -> Flow<List<MonitoringPoint>>,
    val getSensorReadings: (pointId: String) -> Flow<List<SensorReading>>,

    )
