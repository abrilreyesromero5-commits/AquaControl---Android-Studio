package com.example.aquacontrol.presentation.monitoring

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aquacontrol.data.model.MonitoringPoint
import com.example.aquacontrol.di.AppModule
import com.example.aquacontrol.domain.usecase.MonitoringViewModelState
import com.example.aquacontrol.utils.JsonUtils
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MonitoringViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppModule.monitoringPointRepository

    private val _uiState = MutableStateFlow(MonitoringViewModelState())
    val uiState: StateFlow<MonitoringViewModelState> = _uiState.asStateFlow()

    private val republicData = JsonUtils.loadRepublicFromAssets(application.applicationContext)

    private val _selectedMonitoringPoint = MutableStateFlow<MonitoringPoint?>(null)
    val selectedMonitoringPoint: StateFlow<MonitoringPoint?> = _selectedMonitoringPoint.asStateFlow()



    init {
        loadStates()
        observeMonitoringPoints()
    }

    private fun loadStates() {
        val states = republicData?.states?.map { it.nombre } ?: emptyList()
        _uiState.update { it.copy(states = states) }
    }

    fun onStateSelected(state: String) {
        val municipalities = republicData?.states?.find { it.nombre == state }?.municipios?.map { it.nombre } ?: emptyList()
        _uiState.update {
            it.copy(
                selectedState = state,
                municipalities = municipalities,
                selectedMunicipality = null,
                localities = emptyList(),
                selectedLocality = null,
                latitude = null,
                longitude = null
            )
        }
    }

    fun onMunicipalitySelected(municipality: String) {
        val localities = republicData?.states
            ?.find { it.nombre == _uiState.value.selectedState }
            ?.municipios
            ?.find { it.nombre == municipality }
            ?.localidades
            ?.map { it.nombre }
            ?: emptyList()
        _uiState.update {
            it.copy(
                selectedMunicipality = municipality,
                localities = localities,
                selectedLocality = null,
                latitude = null,
                longitude = null
            )
        }
    }

    fun onLocalitySelected(locality: String) {
        val localityObj = republicData?.states
            ?.find { it.nombre == _uiState.value.selectedState }
            ?.municipios
            ?.find { it.nombre == _uiState.value.selectedMunicipality }
            ?.localidades
            ?.find { it.nombre == locality }

        _uiState.update {
            it.copy(
                selectedLocality = locality,
                latitude = localityObj?.latitud,
                longitude = localityObj?.longitud
            )
        }
    }

    fun registerMonitoringPoint(isActive: Boolean = true) {
        // Forzamos isActive = true para nuevos puntos
        val point = MonitoringPoint(
            id = "", // nuevo punto
            state = uiState.value.selectedState ?: "",
            municipality = uiState.value.selectedMunicipality ?: "",
            locality = uiState.value.selectedLocality ?: "",
            latitude = uiState.value.latitude ?: 0.0,
            longitude = uiState.value.longitude ?: 0.0,
            active = true, // Siempre activo al crear
            registrationDate = null // Timestamp.now() en repo
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                repository.addMonitoringPoint(point)
                _uiState.update {
                    it.copy(
                        errorMessage = null,
                        successMessage = "Registro exitoso.",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = e.message,
                        successMessage = null,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun clearSelections() {
        _uiState.update {
            it.copy(
                selectedState = null,
                selectedMunicipality = null,
                selectedLocality = null,
                latitude = null,
                longitude = null,
                errorMessage = null
            )
        }
    }

    fun updateMonitoringPoint(point: MonitoringPoint) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                repository.updateMonitoringPoint(point)
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message, isLoading = false) }
            }
        }
    }

    fun deactivateMonitoringPoint(pointId: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                repository.deactivateMonitoringPoint(pointId)
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message, isLoading = false) }
            }
        }
    }

    private fun observeMonitoringPoints() {
        viewModelScope.launch {
            repository.getAllMonitoringPoints()
                .catch { e -> _uiState.update { it.copy(errorMessage = e.message) } }
                .collect { points ->
                    _uiState.update { it.copy(monitoringPoints = points) }
                }
        }
    }

    fun observeMonitoringPointById(pointId: String): Flow<MonitoringPoint?> {
        return repository.getMonitoringPointByIdFlow(pointId)
    }

    fun clearSuccessMessage() {
        _uiState.update { it.copy(successMessage = null) }
    }
}


