package com.example.aquacontrol.domain.usecase

import com.example.aquacontrol.data.repository.ReportRepository
import javax.inject.Inject

data class GetReportEntriesUseCase @Inject constructor(
    val getReports: GetReports
) {
    class GetReports @Inject constructor(
        private val repository: ReportRepository
    ) {
        suspend operator fun invoke(state: String?, month: Int?, year: Int?) =
            repository.getReportEntries(state, month, year)
    }
}
