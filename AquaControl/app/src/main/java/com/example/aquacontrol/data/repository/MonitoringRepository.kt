package com.example.aquacontrol.data.repository

import com.example.aquacontrol.data.model.MonitoringPoint
import com.example.aquacontrol.data.model.SensorReading
import kotlinx.coroutines.flow.Flow

interface MonitoringRepository {
    suspend fun registerMonitoringPoint(point: MonitoringPoint): Result<Unit>
    suspend fun getAllMonitoringPoints(): Result<List<MonitoringPoint>>
    suspend fun updateMonitoringPoint(point: MonitoringPoint): Result<Unit>
    suspend fun deleteMonitoringPoint(id: String): Result<Unit>
    fun getMonitoringPointsFlow(): Flow<List<MonitoringPoint>>
    fun getSensorReadings(pointId: String): Flow<List<SensorReading>>
}