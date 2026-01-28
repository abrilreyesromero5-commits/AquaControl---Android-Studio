package com.example.aquacontrol.domain.usecase

import com.example.aquacontrol.data.model.MonitoringPoint

data class MonitoringViewModelState(
    val states: List<String> = emptyList(),
    val municipalities: List<String> = emptyList(),
    val localities: List<String> = emptyList(),

    val selectedState: String? = null,
    val selectedMunicipality: String? = null,
    val selectedLocality: String? = null,

    val latitude: Double? = null,
    val longitude: Double? = null,

    val monitoringPoints: List<MonitoringPoint> = emptyList(),

    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val successMessage: String? = null,

)