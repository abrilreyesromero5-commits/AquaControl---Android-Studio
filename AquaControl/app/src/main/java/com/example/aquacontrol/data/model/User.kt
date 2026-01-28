package com.example.aquacontrol.data.model

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
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)


data class SensorReading(
    val ph: Float = 0f,
    val temperature: Float = 0f,
    val turbidity: Float = 0f,
    val timestamp: Long = System.currentTimeMillis()
)