package com.example.aquacontrol.data.repository

import com.example.aquacontrol.data.model.MonitoringPoint
import com.example.aquacontrol.data.model.ReportEntry
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import com.example.aquacontrol.data.model.SensorReading
import kotlinx.coroutines.tasks.await
import java.util.Calendar

class ReportRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ReportRepository {

    override suspend fun getReportEntries(state: String?, month: Int?, year: Int?): List<ReportEntry> {
        val pointsSnapshot = firestore.collection("monitoring_points").get().await()
        val reportEntries = mutableListOf<ReportEntry>()

        for (pointDoc in pointsSnapshot.documents) {
            val point = pointDoc.toObject(MonitoringPoint::class.java) ?: continue

            if (state != null && point.state != state) continue

            val readingsSnapshot = pointDoc.reference.collection("readings").get().await()

            for (readingDoc in readingsSnapshot.documents) {
                val reading = readingDoc.toObject(SensorReading::class.java) ?: continue

                val calendar = Calendar.getInstance().apply { timeInMillis = reading.timestamp }
                if (month != null && calendar.get(Calendar.MONTH) + 1 != month) continue
                if (year != null && calendar.get(Calendar.YEAR) != year) continue

                reportEntries.add(
                    ReportEntry(
                        pointId = point.id,
                        state = point.state,
                        municipality = point.municipality,
                        locality = point.locality,
                        latitude = point.latitude,
                        longitude = point.longitude,
                        reading = reading
                    )
                )
            }
        }
        return reportEntries
    }
}
