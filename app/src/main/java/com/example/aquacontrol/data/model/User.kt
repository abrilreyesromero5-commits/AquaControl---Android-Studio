package com.example.aquacontrol.data.model

import com.google.firebase.Timestamp

data class User(
    val uid: String = "",
    val email: String = "",
    val role: UserRole = UserRole.USER
) {
    enum class UserRole {
        ADMIN, USER
    }
}

data class MonitoringPoint(
    val id: String = "",
    val state: String = "",
    val municipality: String = "",
    val locality: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val active: Boolean = true,
    val registrationDate: Timestamp? = null  // <-- debe estar aquí en el constructor
)


data class SensorReading(
    val ph: Float = 0f,
    val temperature: Float = 0f,
    val turbidity: Float = 0f,
    val timestamp: Long = System.currentTimeMillis()
)

data class ReportEntry(
    val pointId: String,
    val state: String,
    val municipality: String,
    val locality: String,
    val latitude: Double,
    val longitude: Double,
    val reading: SensorReading
)

data class MonitoreoResponse(
    val estado: String,
    val municipio: String,
    val localidad: String,
    val latitud: Double,
    val longitud: Double,
    val ph: Double,
    val temperatura: Double,
    val turbidez: Double,
    val estado_pH: String,
    val estado_temperatura: String,
    val estado_turbidez: String
)

data class ComparacionAnual(
    val year: Int,
    val estado: String,
    val municipio: String,
    val localidad: String,
    val ph: Double,
    val temperatura: Double,
    val turbidez: Double
)
