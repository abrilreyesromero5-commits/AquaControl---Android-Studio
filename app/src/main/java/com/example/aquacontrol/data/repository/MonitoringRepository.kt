package com.example.aquacontrol.data.repository

import com.example.aquacontrol.data.model.MonitoringPoint
import com.example.aquacontrol.data.model.SensorReading
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow

interface MonitoringPointRepository {
    suspend fun addMonitoringPoint(point: MonitoringPoint)
    suspend fun updateMonitoringPoint(point: MonitoringPoint)
    suspend fun deactivateMonitoringPoint(pointId: String)
    fun getAllMonitoringPoints(): Flow<List<MonitoringPoint>>
    fun getMonitoringPointByIdFlow(pointId: String): Flow<MonitoringPoint?>
    fun getSensorReadingsByPointId(pointId: String): Flow<List<SensorReading>>

}