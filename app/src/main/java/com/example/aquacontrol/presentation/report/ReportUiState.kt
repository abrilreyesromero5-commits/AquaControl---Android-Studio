package com.example.aquacontrol.presentation.report

import com.example.aquacontrol.data.model.ReportEntry

data class ReportUiState(
    val isLoading: Boolean = false,
    val reportEntries: List<ReportEntry> = emptyList(),
    val error: String? = null
)
