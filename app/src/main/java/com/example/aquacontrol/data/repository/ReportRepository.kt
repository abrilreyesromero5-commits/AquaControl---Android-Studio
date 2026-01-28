package com.example.aquacontrol.data.repository

import com.example.aquacontrol.data.model.ReportEntry

interface ReportRepository {
    suspend fun getReportEntries(
        state: String? = null,
        month: Int? = null,
        year: Int? = null
    ): List<ReportEntry>
}